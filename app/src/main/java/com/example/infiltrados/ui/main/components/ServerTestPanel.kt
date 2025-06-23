package com.example.infiltrados.ui.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.infiltrados.ui.main.multiplayer.MultiplayerGameViewModel

@Composable
fun ServerTestPanel(viewModel: MultiplayerGameViewModel) {
    val gameRecord by viewModel.game.collectAsState()
    var gameId = remember { mutableStateOf("") }

    TextField(
        value = gameId.value,
        onValueChange = { gameId.value = it },
        label = { Text("Join game") },
        placeholder = { Text("game Id") },
        singleLine = true,
    )

    Button(
        onClick = {
            viewModel.createGame("test")
        }
    ) {
        Text("createGame")
    }
    if (viewModel.isLoading && gameRecord == null) {
        CircularProgressIndicator()
    } else if (gameRecord != null) {
        Column {
            Text("Game ID: ${gameRecord?.id}") // Access properties of GameRecord
            Text("Players: ${viewModel.getPlayers().joinToString()}")
            // ... more UI based on gameRecord
        }
    } else {
        Text("No game data available.")
        // Button to create or join a game
    }
}