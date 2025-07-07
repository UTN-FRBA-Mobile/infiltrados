package com.example.infiltrados.ui.main.multiplayer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infiltrados.models.Player
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.components.AnimatedBackground
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*



@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun VotationScreen(
    mpViewModel: MultiplayerGameViewModel,
    onNavigateToPhase: (MultiplayerPhase) -> Unit
) {
    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)

    if (mpViewModel.isLoading) {
        CircularProgressIndicator()
        return
    }

    val game = mpViewModel.game.value
    val activePlayers = mpViewModel.gameManager?.getActivePlayers() ?: emptyList()
    val currentPlayer = mpViewModel.gameManager?.getPlayerFromName()

    var selectedPlayer by remember { mutableStateOf<Player?>(null) }

    val currentPlayerName = currentPlayer?.name
    val alreadyVoted = game?.voteBy?.contains(currentPlayerName) == true

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        AnimatedBackground()

        Column {
            Text(
                text = "Votación en curso",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            if (!alreadyVoted) {
                activePlayers
                    .filter { it.name != currentPlayerName }
                    .forEach { player ->
                        Button(
                            onClick = { selectedPlayer = player },
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .fillMaxWidth()
                        ) {
                            Text("Votar a ${player.name}")
                        }
                    }

                selectedPlayer?.let {
                    Button(
                        onClick = {
                            mpViewModel.voteForPlayer(it.name)
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Confirmar voto para ${it.name}")
                    }
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Esperando que todos voten...")
                    CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}