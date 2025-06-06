package com.example.infiltrados.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infiltrados.backend.Appwrite
import com.example.infiltrados.backend.GameRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class ServerTestViewModel : ViewModel() {
    // Backing property to avoid state updates from other classes
    private val _game = MutableStateFlow<GameRecord?>(null)
    // The UI collects from this StateFlow to get its state updates
    val game: MutableStateFlow<GameRecord?> = _game

    fun getAppwriteGame(gameId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _game.emit(Appwrite.getGame(gameId))
        }
    }

    fun getLiveGame(gameId: String) {
        viewModelScope.launch (Dispatchers.IO){
            val updatesFlow = Appwrite.subscribe(gameId)
            updatesFlow.collect { _game.value = it }
        }
    }

    fun createGame() {
        viewModelScope.launch (Dispatchers.IO){
            val game = Appwrite.createGame("javier")
            _game.emit(game)
            getLiveGame(game.id)
        }
    }
}