package com.example.infiltrados.ui.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infiltrados.backend.GameRecord
import com.example.infiltrados.models.MultiplayerGameManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class MultiplayerGameViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set


    var gameManager: MultiplayerGameManager? by mutableStateOf(null)

    // This is the StateFlow exposed to the UI
    private val _game = MutableStateFlow<GameRecord?>(null)
    val game: StateFlow<GameRecord?> = _game.asStateFlow()


    fun getPlayers(): List<String> {
        return gameManager!!.getPlayers()
    }


    fun createGame(name: String) {
        Log.d("DEBUG", "Creating online game")
        isLoading = true
        viewModelScope.launch {
            gameManager =
                MultiplayerGameManager.createGame(name, scope = viewModelScope).await()
            isLoading = false
            gameManager!!.gameRecordFlow.onEach { newGameRecord ->
                _game.value = newGameRecord
            }.launchIn(viewModelScope)
        }
    }

    fun removePlayer(name: String) {
        viewModelScope.launch {
            gameManager?.kickPlayer(name)
        }
    }
}