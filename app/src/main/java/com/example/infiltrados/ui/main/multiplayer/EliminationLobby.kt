package com.example.infiltrados.ui.main.multiplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.infiltrados.models.Role
import com.example.infiltrados.ui.main.components.AnimatedBackground


@Composable
fun EliminationLobby(
    mpViewModel: MultiplayerGameViewModel,
    eliminatedContent: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Fuiste eliminado 😔",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Esperá al final de la partida para conocer el resultado.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
    },
    content: @Composable () -> Unit
) {
    val game = mpViewModel.game.collectAsState().value
    val eliminatedPlayers = game?.players?.filter { it.role == Role.ELIMINATED } ?: emptyList()
    val currentPlayer = mpViewModel.gameManager?.getPlayerFromName()
    val currentPlayerName = currentPlayer?.name
    val isCurrentPlayerEliminated = eliminatedPlayers.any { it.name == currentPlayerName }

    if (isCurrentPlayerEliminated) {
        eliminatedContent()
    } else {
        content()
    }
}
