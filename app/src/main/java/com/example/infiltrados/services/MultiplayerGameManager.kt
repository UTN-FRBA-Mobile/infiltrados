package com.example.infiltrados.services

import android.content.Context
import android.util.Log
import com.example.infiltrados.models.GameRecord
import com.example.infiltrados.models.Player
import com.example.infiltrados.models.Role
import com.example.infiltrados.ui.main.Destination
import com.example.infiltrados.ui.main.multiplayer.MultiplayerGameViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

enum class MultiplayerPhase(val destination: Destination) {
    LOBBY(Destination.OnlineLobby),
    REVEAL(Destination.Reveal),
    DISCUSSION(Destination.Discussion),
    VOTE(Destination.Vote),
    MR_WHITE_GUESS(Destination.MrWhiteGuess),
    END_GAME(Destination.EndGame),
    PLAYER_ELIMINATED(Destination.PlayerEliminated)
}


class MultiplayerGameManager(
    val playerName: String,
    val isHost: Boolean,
    initialGameRecord: GameRecord,
    val incomingGameRecordFlow: Flow<GameRecord>,
    var numUndercover: Int = 1,
    var includeMrWhite: Boolean = true,
    var players: List<Player> = emptyList(),
    val scope: CoroutineScope
) {
    val gameRecordFlow: StateFlow<GameRecord> = incomingGameRecordFlow
        .catch { e ->
            Log.e(
                "MultiplayerGameManager",
                "MultiplayerGameManager: Error in shared gameRecordFlow",
                e
            )
        }
        .onEach {
            Log.d("MultiplayerGameManager", "MultiplayerGameManager: GameRecord updated: $it")
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialGameRecord
        )


    val game: GameRecord
        get() = gameRecordFlow.value


    companion object Factory {
        fun createGame(
            hostName: String,
            scope: CoroutineScope,
            viewModel: MultiplayerGameViewModel
        ): Deferred<MultiplayerGameManager> {
            return scope.async(Dispatchers.IO) {
                val createdGame = AppwriteService.createGame(hostName)
                val gameSuscription = AppwriteService.subscribe(createdGame.id)
                val manager = MultiplayerGameManager(
                    hostName,
                    true,
                    createdGame,
                    gameSuscription,
                    scope = scope
                )
                viewModel.setCurrentPlayerName(hostName)
                manager
            }
        }

        fun joinGame(
            gameId: String,
            playerName: String,
            scope: CoroutineScope,
            viewModel: MultiplayerGameViewModel
        ): Deferred<MultiplayerGameManager> {
            return scope.async {
                AppwriteService.joinGame(gameId, playerName)
                val game = AppwriteService.getGame(gameId)
                val gameSuscription = AppwriteService.subscribe(gameId)
                val manager = MultiplayerGameManager(
                    playerName,
                    false,
                    game!!,
                    gameSuscription,
                    scope = scope
                )
                viewModel.setCurrentPlayerName(playerName)
                manager
            }
        }
    }

    private fun updateGame(updatedGame: GameRecord): Deferred<GameRecord> {
        return scope.async(Dispatchers.IO) {
            try {
                AppwriteService.updateGame(updatedGame)
            } catch (e: Exception) {
                Log.e("MultiplayerGameManager", "Error updating game", e)
                throw e
            }

        }
    }

    fun kickPlayer(playerName: String): Deferred<GameRecord> {
        if (!isHost || playerName == this.playerName)
            return CompletableDeferred(game)
        
        val newPlayers = game.players.filter { it.name != playerName }
        val updated = game.copy(players = newPlayers)
        return updateGame(updated)
    }

    fun canStartGame(): Boolean {
        val numCitizens = game.players.size - numUndercover - if (includeMrWhite) 1 else 0
        return game.players.size >= 3 && (numUndercover > 0 || includeMrWhite) && numCitizens >= 2
    }

    fun startGame(context: Context, spanish: Boolean): Deferred<GameRecord> {
        val shuffledNames = game.players.shuffled()

        val roles = mutableListOf<Role>()
        repeat(numUndercover) { roles.add(Role.UNDERCOVER) }
        if (includeMrWhite) roles.add(Role.MR_WHITE)
        while (roles.size < game.players.size) roles.add(Role.CIUDADANO)
        roles.shuffle()

        players = shuffledNames.mapIndexed { index, player ->
            Player(player.name, roles[index])
        }


        val wordPairs = WordLoader.loadWordPairs(context, spanish)
        val randomPair = wordPairs.random()

        val updated = game.copy(
            phase = MultiplayerPhase.REVEAL,
            players = players,
            word1 = randomPair.word1,
            word2 = randomPair.word2,
            voteBy = emptyList(),
            lastEliminated = Player("", Role.CIUDADANO)
        )
        return updateGame(updated)
    }

    fun getWordForPlayer(player: Player?): String {
        return when (player?.role) {
            Role.UNDERCOVER -> game.word2
            Role.CIUDADANO -> game.word1
            else -> {
                ""
            }
        }
    }


    fun startDiscussion(): Deferred<GameRecord> {
        val playersShuffled = players.shuffled()
        val updated = game.copy(phase = MultiplayerPhase.DISCUSSION, players = playersShuffled)
        return updateGame(updated)
    }

    fun startVoting(): Deferred<GameRecord> {
        val updated = game.copy(phase = MultiplayerPhase.VOTE)
        return updateGame(updated)
    }

    fun startReveal(): Deferred<GameRecord> {
        val updated = game.copy(phase = MultiplayerPhase.REVEAL)
        return updateGame(updated)
    }

    fun getPlayerFromName(): Player? {
        return game.players.find { it.name == playerName }
    }

    fun getActivePlayers(): List<Player> {
        return game.players.filter { it.role != Role.ELIMINATED }
    }

    fun isPlayerEliminated(player: Player?): Boolean {
        return !getActivePlayers().contains(player)
    }

    fun isMrWhiteGuessCorrect(guess: String): Boolean {
        return game.word1 == guess
    }

    fun mrWhiteGuess(): Deferred<GameRecord> {
        val updated = game.copy(phase = MultiplayerPhase.MR_WHITE_GUESS)
        return updateGame(updated)
    }

    fun mrWhiteWin(mrWhite: Player?): Deferred<GameRecord> {
        eliminatePlayersExcept(mrWhite)

        val updated = game.copy(phase = MultiplayerPhase.END_GAME, players = players)
        return updateGame(updated)
    }

    fun eliminatePlayersExcept(player: Player?) {
        players.forEach {
            if (it.name != player?.name) {
                it.role = Role.ELIMINATED
            }
        }
    }

    fun endGame(): Deferred<GameRecord> {
        val updated = game.copy(phase = MultiplayerPhase.END_GAME)
        return updateGame(updated)
    }

    fun resetGame(): Deferred<GameRecord> {
        val updated = game.copy(
            phase = MultiplayerPhase.LOBBY,
            //players = emptyList()
        )
        return updateGame(updated)
    }

    fun eliminateMrWhite(): Deferred<GameRecord> {
        val mrWhitePlayer = game.players.find { it.role == Role.MR_WHITE }
            ?: throw IllegalStateException("No se encontró al jugador Mr. White")

        val updatedPlayers = game.players.map { player ->
            if (player.name == mrWhitePlayer.name) {
                player.copy(
                    isEliminated = true,
                    role = Role.ELIMINATED,
                    votes = 0,
                    voteBy = emptyList()
                )
            } else {
                player.copy(
                    votes = 0,
                    voteBy = emptyList()
                )
            }
        }

        val updatedGame = game.copy(
            players = updatedPlayers,
            lastEliminated = mrWhitePlayer.copy(
                isEliminated = true,
                role = Role.ELIMINATED,
                votes = 0,
                voteBy = emptyList()
            ),
            phase = MultiplayerPhase.PLAYER_ELIMINATED
        )

        return updateGame(updatedGame)
    }


    fun getWinners(): String {
        val activePlayers = getActivePlayers()
        val activeRoles = activePlayers.map { it.role }

        val mrWhiteAlive = activeRoles.contains(Role.MR_WHITE)
        val undercoverCount = activeRoles.count { it == Role.UNDERCOVER }
        val citizenCount = activeRoles.count { it == Role.CIUDADANO }

        return when {
            activePlayers.size == 1 && activeRoles.contains(Role.MR_WHITE) -> Role.MR_WHITE.toString()

            !mrWhiteAlive && undercoverCount > 0 && citizenCount <= undercoverCount ->
                Role.UNDERCOVER.toString()

            !mrWhiteAlive && undercoverCount == 0 && citizenCount > 0 ->
                Role.CIUDADANO.toString()

            else -> ""
        }
    }


    fun gameContinues(): Boolean {
        return getWinners() == ""
    }

    fun voteForPlayer(votedName: String): Deferred<GameRecord> {
        if (game.phase != MultiplayerPhase.VOTE) {
            throw IllegalStateException("No se puede votar en este momento")
        }

        val voterName = playerName

        val updatedPlayers = game.players.map { player ->
            if (player.name == votedName) {
                player.copy(votes = player.votes + 1)
            } else {
                player
            }
        }

        val updated = game.copy(
            players = updatedPlayers,
            voteBy = game.voteBy + voterName
        )

        return updateGame(updated)
    }


    fun finishVotingAndEliminate(): Deferred<GameRecord> = scope.async {
        if (game.phase != MultiplayerPhase.VOTE) {
            throw IllegalStateException("No se puede finalizar la votación en este momento")
        }

        val activePlayers = getActivePlayers()
        val totalVotes = game.players.sumOf { it.votes }

        if (totalVotes < activePlayers.size) {
            throw IllegalStateException("Todavía no votaron todos los jugadores")
        }

        val maxVotes = game.players.maxOfOrNull { it.votes } ?: 0
        val candidates = game.players.filter { it.votes == maxVotes && !it.isEliminated }
        val eliminated = candidates.randomOrNull()
            ?: throw IllegalStateException("No se pudo determinar al eliminado")

        // 🔍 Guardamos su rol original antes de modificarlo
        val originalRole = eliminated.role

        // Si era MR.WHITE, cambiamos de fase a su guess antes de eliminar
        if (originalRole == Role.MR_WHITE) {
            return@async mrWhiteGuess().await()
        }

        // 🔁 Continuar con eliminación normal
        val newPlayers = game.players.map { player ->
            when (player.name) {
                eliminated.name -> player.copy(
                    isEliminated = true,
                    role = Role.ELIMINATED,
                    votes = 0
                )

                else -> player.copy(votes = 0)
            }
        }

        val updated = game.copy(
            phase = MultiplayerPhase.PLAYER_ELIMINATED,
            players = newPlayers,
            voteBy = emptyList(),
            lastEliminated = eliminated
        )

        val updatedGame = updateGame(updated).await()

        players = updatedGame.players

        return@async updatedGame
    }


}