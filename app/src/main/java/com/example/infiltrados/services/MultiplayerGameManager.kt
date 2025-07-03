package com.example.infiltrados.services

import android.util.Log
import com.example.infiltrados.models.GameRecord
import com.example.infiltrados.models.Player
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
                MultiplayerGameManager(hostName, true, createdGame, gameSuscription, scope)
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
                MultiplayerGameManager(playerName, false, game!!, gameSuscription, scope)
            }
        }

    }


    fun getPlayers(): List<String> {
        return game.players
    }

    private fun updateGame(updatedGame: GameRecord): Deferred<GameRecord> {
        return scope.async(Dispatchers.IO) {
            AppwriteService.updateGame(updatedGame)
        }
    }

    fun kickPlayer(playerName: String): Deferred<GameRecord> {
        val newPlayers = game.players.filter { it != playerName }
        val updated = game.copy(players = newPlayers)
        return updateGame(updated)
    }

    suspend fun startGame(): Deferred<GameRecord> {
        // TODO Validate if it's possible to start the game
        val updated = game.copy(phase = MultiplayerPhase.REVEAL)
        return updateGame(updated)
    }

}

object CurrentGame {
    lateinit var manager: MultiplayerGameManager
    fun init(gameManager: MultiplayerGameManager) {
        manager = gameManager
    }
}