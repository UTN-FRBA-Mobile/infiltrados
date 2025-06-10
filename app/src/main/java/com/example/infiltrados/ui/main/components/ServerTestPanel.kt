package com.example.infiltrados.ui.main.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.infiltrados.ui.main.ServerTestViewModel

@Composable
fun ServerTestPanel() {
    val viewModel = viewModel<ServerTestViewModel>()
    val gameRecord = viewModel.game.collectAsState()
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
            viewModel.getAppwriteGame(gameId.value)
        }
    ) {
        Text("query api")
    }

    Button(
        onClick = {
            viewModel.getLiveGame(gameId.value)
        }
    ) {
        Text("live")
    }

    Button(
        onClick = {
            viewModel.createGame("alguien")
        }
    ) {
        Text("createGame")
    }
    Text(gameRecord.value.toString())
}