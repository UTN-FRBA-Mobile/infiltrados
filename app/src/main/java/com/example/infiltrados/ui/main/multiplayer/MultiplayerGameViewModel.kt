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

    val isHost: Boolean
        get() = gameManager?.isHost == true

    // This is the StateFlow exposed to the UI
    private val _game = MutableStateFlow<GameRecord?>(null)
    val game: StateFlow<GameRecord?> = _game.asStateFlow()

    private val _phase = Channel<MultiplayerPhase>()
    val phase = _phase.receiveAsFlow()

    private val _error = Channel<String>()
    val error = _error.receiveAsFlow()

    private fun getPhaseFromGameRecord(record: GameRecord?): MultiplayerPhase {
        return record?.phase ?: MultiplayerPhase.LOBBY
    }

    private val gameUpdateCollector = { newGameRecord: GameRecord ->
        Log.d("GAMERECORDFLOW", "Game record updated: $newGameRecord")
        _game.value = newGameRecord
        viewModelScope.launch(Dispatchers.IO) { _phase.send(getPhaseFromGameRecord(newGameRecord)) }
        Unit
    }


    fun createGame(name: String) {
        Log.d("DEBUG", "Creating online game")
        isLoading = true
        viewModelScope.launch {
            gameManager =
                MultiplayerGameManager.Factory.createGame(name, scope = viewModelScope).await()
            isLoading = false
            gameManager!!.gameRecordFlow.onEach(gameUpdateCollector).launchIn(viewModelScope)
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
                gameManager!!.gameRecordFlow.onEach(gameUpdateCollector).launchIn(viewModelScope)
            } catch (e: Exception) {
                Log.e("MultiplayerGameViewModel", "Error joining game", e)
                _error.send("Error joining game: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun startDiscussion() {
        viewModelScope.launch {
            isLoading = true
            gameManager!!.startDiscussion().await()
            isLoading = false
        }
    }

    fun startVoting() {
        viewModelScope.launch {
            isLoading = true
            gameManager!!.startVoting().await()
            isLoading = false
        }
    }

    fun startReveal() {
        viewModelScope.launch {
            isLoading = true
            gameManager!!.startReveal().await()
            isLoading = false
        }
    }

    fun eliminatePlayer(player: Player?) {
        viewModelScope.launch {
            isLoading = true
            gameManager!!.eliminatePlayer(player).await()
            isLoading = false
        }
    }

    fun mrWhiteGuess() {
        viewModelScope.launch {
            isLoading = true
            gameManager!!.mrWhiteGuess().await()
            isLoading = false
        }
    }

    fun mrWhiteWin(mrWhite: Player?){
        viewModelScope.launch {
            isLoading = true
            gameManager!!.mrWhiteWin(mrWhite).await()
            isLoading = false
        }
    }

    fun endGame() {
        viewModelScope.launch {
            isLoading = true
            gameManager!!.endGame().await()
            isLoading = false
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            isLoading = true
            gameManager?.resetGame()?.await()
            isLoading = false
        }
    }

}