package com.example.infiltrados.ui.main.multiplayer

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infiltrados.models.GameRecord
import com.example.infiltrados.models.Player
import com.example.infiltrados.services.MultiplayerGameManager
import com.example.infiltrados.services.MultiplayerPhase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MultiplayerGameViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var gameManager: MultiplayerGameManager? by mutableStateOf(null)

    var numUndercover by mutableStateOf(1)
    var includeMrWhite by mutableStateOf(true)

    val isHost: Boolean
        get() = gameManager?.isHost == true

    private val _game = MutableStateFlow<GameRecord?>(null)
    val game: StateFlow<GameRecord?> = _game.asStateFlow()

    private val _phase = Channel<MultiplayerPhase>()
    val phase = _phase.receiveAsFlow()

    private val _error = Channel<String>()
    val error = _error.receiveAsFlow()

    private fun getPhaseFromGameRecord(record: GameRecord?): MultiplayerPhase {
        return record?.phase ?: MultiplayerPhase.LOBBY
    }

    // Lambda explícita con tipo
    private val gameUpdateCollector: (GameRecord) -> Unit = { newGameRecord ->
        _game.value = newGameRecord
        viewModelScope.launch(Dispatchers.IO) {
            _phase.send(getPhaseFromGameRecord(newGameRecord))
        }
    }

    fun createGame(name: String) {
        isLoading = true
        viewModelScope.launch {
            gameManager = MultiplayerGameManager.Factory.createGame(name, viewModelScope).await()
            isLoading = false
            gameManager!!.gameRecordFlow
                .onEach { gameUpdateCollector(it) } // ← lambda explícita
                .launchIn(viewModelScope)
        }
    }

    fun removePlayer(name: String) {
        viewModelScope.launch {
            gameManager?.kickPlayer(name)
        }
    }

    fun startGame(context: Context, spanish: Boolean) {
        viewModelScope.launch {
            isLoading = true
            gameManager?.numUndercover = numUndercover
            gameManager?.includeMrWhite = includeMrWhite
            gameManager!!.startGame(context, spanish).await()
            isLoading = false
        }
    }



    fun joinGame(gameId: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                gameManager =
                    MultiplayerGameManager.Factory.joinGame(gameId, name, viewModelScope).await()
                gameManager!!.gameRecordFlow
                    .onEach { gameUpdateCollector(it) } // ← lambda explícita
                    .launchIn(viewModelScope)
            } catch (e: Exception) {
                _error.send("Error joining game: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }



    fun canStartGame(): Boolean {
        val playersCount = gameManager?.game?.players?.size ?: 0
        val numCitizens = playersCount - numUndercover - if (includeMrWhite) 1 else 0
        return playersCount >= 3 && (numUndercover > 0 || includeMrWhite) && numCitizens >= 2
    }

    fun startVoting() {
        viewModelScope.launch {
            try {
                isLoading = true
                val updatedGame = gameManager?.startVoting()?.await()
                if (updatedGame != null) {
                    gameUpdateCollector(updatedGame)
                }
            } catch (e: Exception) {
                _error.send("Error starting voting: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    fun resetGame() {
        viewModelScope.launch {
            try {
                isLoading = true
                val updatedGame = gameManager?.resetGame()?.await()
                if (updatedGame != null) {
                    gameUpdateCollector(updatedGame)
                }
            } catch (e: Exception) {
                _error.send("Error resetting game: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    fun mrWhiteWin(player: Player?) {
        viewModelScope.launch {
            try {
                isLoading = true
                val updatedGame = gameManager?.mrWhiteWin(player)?.await()
                if (updatedGame != null) {
                    gameUpdateCollector(updatedGame)
                }
            } catch (e: Exception) {
                _error.send("Error: Mr. White no pudo ganar. ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun eliminatePlayer(player: Player?) {
        viewModelScope.launch {
            try {
                isLoading = true
                val updatedGame = gameManager?.eliminatePlayer(player)?.await()
                if (updatedGame != null) {
                    gameUpdateCollector(updatedGame)
                }
            } catch (e: Exception) {
                _error.send("Error eliminando jugador: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun startDiscussion() {
        viewModelScope.launch {
            try {
                isLoading = true
                val updatedGame = gameManager?.startDiscussion()?.await()
                if (updatedGame != null) {
                    gameUpdateCollector(updatedGame)
                }
            } catch (e: Exception) {
                _error.send("Error iniciando discusión: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun endGame() {
        viewModelScope.launch {
            try {
                isLoading = true
                val updatedGame = gameManager?.endGame()?.await()
                if (updatedGame != null) {
                    gameUpdateCollector(updatedGame)
                }
            } catch (e: Exception) {
                _error.send("Error finalizando el juego: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun mrWhiteGuess() {
        viewModelScope.launch {
            try {
                isLoading = true
                val updatedGame = gameManager?.mrWhiteGuess()?.await()
                if (updatedGame != null) {
                    gameUpdateCollector(updatedGame)
                }
            } catch (e: Exception) {
                _error.send("Error en el intento de adivinanza de Mr. White: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


}
