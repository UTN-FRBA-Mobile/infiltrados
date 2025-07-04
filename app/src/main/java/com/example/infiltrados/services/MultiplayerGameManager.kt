package com.example.infiltrados.services

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

    fun startGame(): Deferred<GameRecord> {
        // TODO Validate if it's possible to start the game
        if (!canStartGame()) {
            throw IllegalStateException("Cannot start game")
        }

        // Barajamos los jugadores
        val shuffledNames = game.players.shuffled()

        // Asignamos roles según la cantidad de jugadores
        val roles = mutableListOf<Role>()

        repeat(numUndercover) { roles.add(Role.UNDERCOVER) }
        if (includeMrWhite) roles.add(Role.MR_WHITE)
        while (roles.size < game.players.size) roles.add(Role.CIUDADANO)
        roles.shuffle()

        // Creamos la lista de jugadores con sus roles y palabras
        players = shuffledNames.mapIndexed { index, player ->
            Player(player.name, roles[index])
        }


        val updated = game.copy(phase = MultiplayerPhase.REVEAL, players = players)
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

    fun getPlayerFromName(): Player? {
        return game.players.find { it.name == playerName }
    }

    fun getActivePlayers(): List<Player> {
        return game.players.filter { it.role != Role.ELIMINATED }
    }

    fun isPlayerEliminated(): Boolean{
        val player = getPlayerFromName() ?: return false
        return !getActivePlayers().contains(player)
    }

    fun endGame(): Deferred<GameRecord> {
        val updated = game.copy(phase = MultiplayerPhase.END_GAME)
        return updateGame(updated)
    }

    fun eliminatePlayer(player: Player): Deferred<GameRecord> {
        //TODO: Agregar logica para eliminar al jugador
        val updated = game.copy(phase = MultiplayerPhase.PLAYER_ELIMINATED)
        return updateGame(updated)
    }

}