package com.example.infiltrados.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infiltrados.backend.Appwrite
import com.example.infiltrados.backend.GameRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LobbyViewModel : ViewModel() {
    // Backing property to avoid state updates from other classes
    private val _game = MutableStateFlow<GameRecord?>(null)
    // The UI collects from this StateFlow to get its state updates
    val game: MutableStateFlow<GameRecord?> = _game
    val gameId = "682a6c190001c178935a"

    fun getAppwriteGame() {
        viewModelScope.launch(Dispatchers.IO) {
            _game.emit(Appwrite.getGame(gameId))
        }
    }

    fun getLiveGame() {
        viewModelScope.launch (Dispatchers.IO){
        val updatesFlow = Appwrite.subscribe(gameId)
        updatesFlow.collect { _game.value = it }
        }
    }
}