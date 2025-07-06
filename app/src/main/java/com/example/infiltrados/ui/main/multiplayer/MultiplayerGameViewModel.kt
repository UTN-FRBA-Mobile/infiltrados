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

    var lastEliminatedPlayer by mutableStateOf<Pair<String, Role>?>(null)
        private set

    // Nueva variable de votos
    private val votes = mutableMapOf<String, Int>()

    private fun getPhaseFromGameRecord(record: GameRecord?): MultiplayerPhase {
        return record?.phase ?: MultiplayerPhase.LOBBY
    }

    private val gameUpdateCollector: (GameRecord) -> Unit = { newGameRecord ->
        _game.value = newGameRecord

        val activeCount = gameManager?.getActivePlayers()?.size ?: 0

        if (newGameRecord.phase == MultiplayerPhase.VOTE) {
            val alreadyVoted = newGameRecord.votedBy.contains(gameManager?.playerName)
            hasVoted = alreadyVoted
        }



        // Verificar si todos votaron
        if (newGameRecord.phase == MultiplayerPhase.VOTE &&
            newGameRecord.votes.size == activeCount &&
            isHost // solo el host puede ejecutar la lógica de finalización
        ) {
            viewModelScope.launch {
                try {
                    finishVoting()
                } catch (e: Exception) {
                    _error.send("Error finalizando votación automáticamente: ${e.message}")
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _phase.send(getPhaseFromGameRecord(newGameRecord))
        }
    }



    var voteCounts by mutableStateOf<Map<String, Int>>(emptyMap())
        private set


    var votedPlayers by mutableStateOf<Set<String>>(emptySet())
        private set

    fun voteFor(playerName: String) {
        val current = voteCounts.toMutableMap()
        current[playerName] = (current[playerName] ?: 0) + 1
        voteCounts = current

        votedPlayers = votedPlayers + (gameManager?.playerName ?: "")


        val activePlayers = gameManager?.getActivePlayers() ?: emptyList()
        if (votedPlayers.size == activePlayers.size) {
            val maxVotes = voteCounts.maxByOrNull { it.value }?.value ?: 0
            val mostVoted = voteCounts.filter { it.value == maxVotes }.keys.randomOrNull()

            val player = activePlayers.find { it.name == mostVoted }

            if (player?.role == Role.MR_WHITE) {
                mrWhiteGuess()
            } else {
                eliminatePlayer(player)
            }


            voteCounts = emptyMap()
            votedPlayers = emptySet()
        }
    }

    fun eliminatePlayer(player: Player?) {
        viewModelScope.launch {
            try {
                isLoading = true
                val name = player?.name ?: "Desconocido"
                val role = player?.role ?: Role.NADA
                lastEliminatedPlayer = Pair(name, role)
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

    fun voteForPlayer(name: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                hasVoted = true
                val updatedGame = gameManager?.voteForPlayer(name)?.await()
                if (updatedGame != null) {
                    gameUpdateCollector(updatedGame)
                }
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
                val updatedGame = gameManager?.finishVotingAndEliminate()?.await()
                if (updatedGame != null) {
                    gameUpdateCollector(updatedGame)
                }
            } catch (e: Exception) {
                _error.send("Error al finalizar la votación: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }



}
