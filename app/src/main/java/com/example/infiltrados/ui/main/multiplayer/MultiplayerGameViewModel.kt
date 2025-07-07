package com.example.infiltrados.ui.main.multiplayer

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infiltrados.models.GameRecord
import com.example.infiltrados.models.Player
import com.example.infiltrados.models.Role
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

    var hasVoted by mutableStateOf(false)
        private set

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

    var spanish by mutableStateOf(true)


    // Nueva variable de votos
    private val votes = mutableMapOf<String, Int>()

    private fun getPhaseFromGameRecord(record: GameRecord?): MultiplayerPhase {
        return record?.phase ?: MultiplayerPhase.LOBBY
    }

    private val gameUpdateCollector: (GameRecord) -> Unit = { newGameRecord ->
        _game.value = newGameRecord

        if (newGameRecord.phase == MultiplayerPhase.VOTE) {
            val currentName = gameManager?.playerName


            hasVoted = newGameRecord.players.any { it.voteBy.contains(currentName) }

            if (isHost) {
                val activeCount = gameManager?.getActivePlayers()?.size ?: 0
                val totalVotes = newGameRecord.players.sumOf { it.votes }

                if (totalVotes == activeCount) {
                    viewModelScope.launch {
                        try {
                            finishVoting()
                        } catch (e: Exception) {
                            _error.send("Error finalizando votación automáticamente: ${e.message}")
                        }
                    }
                }
            }
        } else {

            hasVoted = false
        }

        viewModelScope.launch(Dispatchers.IO) {
            _phase.send(getPhaseFromGameRecord(newGameRecord))
        }
    }


    var voteCounts by mutableStateOf<Map<String, Int>>(emptyMap())
        private set


    var votedPlayers by mutableStateOf<Set<String>>(emptySet())
        private set

    var currentPlayerName: String? = null
        private set

    fun setCurrentPlayerName(name: String) {
        currentPlayerName = name
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


    fun createGame(name: String) {
        isLoading = true
        viewModelScope.launch {
            try {
                gameManager =
                    MultiplayerGameManager.Factory.createGame(name, viewModelScope,this@MultiplayerGameViewModel).await()
                gameManager!!.gameRecordFlow
                    .onEach { gameUpdateCollector(it) } // ← lambda explícita
                    .launchIn(viewModelScope)
            } catch (e: Exception) {
                _error.send("Error creating game: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun removePlayer(name: String) {
        viewModelScope.launch {
            gameManager?.kickPlayer(name)
        }
    }

    fun startGame(context: Context, spanish: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                gameManager?.numUndercover = numUndercover
                gameManager?.includeMrWhite = includeMrWhite
                gameManager!!.startGame(context, spanish).await()
            } catch (e: Exception) {
                _error.send("Error starting game: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


    fun joinGame(gameId: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                gameManager =
                    MultiplayerGameManager.Factory.joinGame(gameId, name, viewModelScope,this@MultiplayerGameViewModel).await()
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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                gameManager?.startVoting()?.await()
            } catch (e: Exception) {
                _error.send("Error starting voting: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun resetGame() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading = true
                gameManager?.resetGame()?.await()
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
                gameManager?.mrWhiteWin(player)?.await()
            } catch (e: Exception) {
                _error.send("Error: Mr. White no pudo ganar. ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


    fun startDiscussion() {
        viewModelScope.launch {
            try {
                isLoading = true
                gameManager?.startDiscussion()?.await()
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
                gameManager?.endGame()?.await()
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
                gameManager?.mrWhiteGuess()?.await()
            } catch (e: Exception) {
                _error.send("Error en el intento de adivinanza de Mr. White: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun voteForPlayer(name: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                hasVoted = true
                gameManager?.voteForPlayer(name)?.await()
            } catch (e: Exception) {
                _error.send("Error al votar: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


    fun finishVoting() {
        viewModelScope.launch {
            try {
                isLoading = true
                gameManager?.finishVotingAndEliminate()?.await()
            } catch (e: Exception) {
                _error.send("Error al finalizar la votación: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    val lastEliminatedPlayer: Pair<String, Role>?
        get() {
            val eliminated = game.value?.lastEliminated
            return if (eliminated != null && eliminated.name.isNotBlank()) {
                Pair(eliminated.name, eliminated.role)
            } else {
                null
            }
        }




}