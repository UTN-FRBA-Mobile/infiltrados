package com.example.infiltrados.ui.main.multiplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun EliminationLobby(
    mpViewModel: MultiplayerGameViewModel,
    eliminatedContent: @Composable () -> Unit = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Esperá al final de la partida",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    },
    content: @Composable () -> Unit
) {
    val game = mpViewModel.game.collectAsState().value
    val currentPlayerName = mpViewModel.currentPlayerName

    val currentPlayer = remember(game, currentPlayerName) {
        game?.players?.find { it.name == currentPlayerName }
    }

    if (currentPlayer?.isEliminated == true) {
        eliminatedContent()
    } else {
        content()
    }
}
