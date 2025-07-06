package com.example.infiltrados.services

import android.content.Context
import android.util.Log
import com.example.infiltrados.models.GameRecord
import com.example.infiltrados.models.Player
import com.example.infiltrados.models.Role
import com.example.infiltrados.ui.main.Destination
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
            scope: CoroutineScope
        ): Deferred<MultiplayerGameManager> {
            return scope.async(Dispatchers.IO) {
                val createdGame = AppwriteService.createGame(hostName)
                val gameSuscription = AppwriteService.subscribe(createdGame.id)
                MultiplayerGameManager(hostName, true, createdGame, gameSuscription, scope = scope)
            }
        }

        fun joinGame(
            gameId: String,
            playerName: String,
            scope: CoroutineScope
        ): Deferred<MultiplayerGameManager> {
            return scope.async {
                AppwriteService.joinGame(gameId, playerName)
                val game = AppwriteService.getGame(gameId)
                val gameSuscription = AppwriteService.subscribe(gameId)
                MultiplayerGameManager(
                    playerName, false, game!!, gameSuscription,
                    scope = scope
                )
            }
        }

    }
    
    private fun updateGame(updatedGame: GameRecord): Deferred<GameRecord> {
        return scope.async(Dispatchers.IO) {
            AppwriteService.updateGame(updatedGame)
        }
    }

    fun kickPlayer(playerName: String): Deferred<GameRecord> {
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
            word2 = randomPair.word2
        )
        return updateGame(updated)
    }


    fun startDiscussion(): Deferred<GameRecord> {
        val updated = game.copy(phase = MultiplayerPhase.DISCUSSION)
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

    fun isPlayerEliminated(player: Player?): Boolean{
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

    fun eliminatePlayer(player: Player?): Deferred<GameRecord> {

        players.find { it.name == player?.name }?.let {
             it.role = Role.ELIMINATED
        }

        val updated = game.copy(phase = MultiplayerPhase.PLAYER_ELIMINATED, players = players)
        return updateGame(updated)
    }

    fun getWinners(): String {
        val activePlayers = getActivePlayers()
        val activeRoles = activePlayers.map { it.role }

        val mrWhiteAlive = activeRoles.contains(Role.MR_WHITE)
        val undercoverAlive = activeRoles.contains(Role.UNDERCOVER)
        val citizensAlive = activeRoles.contains(Role.CIUDADANO)

        return when {
            activePlayers.size == 1 && activeRoles.contains(Role.MR_WHITE) -> Role.MR_WHITE.toString()
            undercoverAlive && !mrWhiteAlive && !citizensAlive -> Role.UNDERCOVER.toString()
            !undercoverAlive && !mrWhiteAlive && citizensAlive -> Role.CIUDADANO.toString()
            else -> ""
        }
    }

    fun gameContinues(): Boolean {
        return getWinners() == ""
    }

    fun voteForPlayer(votedName: String): Deferred<GameRecord> {
        val updatedVotes = game.votes + votedName
        val updatedVotedBy = game.votedBy + playerName

        val updated = game.copy(
            votes = updatedVotes,
            votedBy = updatedVotedBy
        )

        return updateGame(updated)
    }



    fun finishVotingAndEliminate(): Deferred<GameRecord> {
        val totalVoters = getActivePlayers().size
        if (game.votes.size < totalVoters) {
            throw IllegalStateException("Todavía no votaron todos los jugadores")
        }

        // Conteo de votos
        val voteCounts = game.votes.groupingBy { it }.eachCount()
        val maxVotes = voteCounts.values.maxOrNull() ?: 0
        val mostVoted = voteCounts.filterValues { it == maxVotes }.keys.random()

        val eliminated = players.find { it.name == mostVoted }
        eliminated?.role = Role.ELIMINATED

        val updated = game.copy(
            phase = MultiplayerPhase.PLAYER_ELIMINATED,
            players = players,
            votes = emptyList(),
            votedBy = emptyList()
        )

        return updateGame(updated)
    }



}