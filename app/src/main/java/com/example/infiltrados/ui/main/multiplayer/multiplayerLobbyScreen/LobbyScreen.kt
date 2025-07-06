package com.example.infiltrados.ui.main.multiplayer.multiplayerLobbyScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.infiltrados.R
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.components.ButtonWithLoading
import com.example.infiltrados.ui.main.multiplayer.MultiplayerGameViewModel
import com.example.infiltrados.ui.main.multiplayer.ObserveMultiplayerPhase
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove





@Composable
fun OnlineLobbyScreen(
    mpViewModel: MultiplayerGameViewModel,
    onBackToLobby: () -> Unit,
    onNavigateToPhase: (MultiplayerPhase) -> Unit,
    spanish: Boolean
) {
    val context = LocalContext.current

    if (mpViewModel.gameManager == null && !mpViewModel.isLoading) {
        onBackToLobby()
        return
    }

    if (mpViewModel.isLoading) {
        CircularProgressIndicator()
        return
    }

    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)

    val gameRecord by mpViewModel.game.collectAsState()
    val players = gameRecord?.players ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fondo visible
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.players_label),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(16.dp))

        PlayerList(players.map { it.name }, mpViewModel.isHost) {
            mpViewModel.removePlayer(it)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (mpViewModel.isHost) {
            Text(
                text = stringResource(R.string.undercover_label),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    if (mpViewModel.numUndercover > 0) mpViewModel.numUndercover--
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Menos")
                }
                Text(
                    text = mpViewModel.numUndercover.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = { mpViewModel.numUndercover++ }) {
                    Icon(Icons.Default.Add, contentDescription = "Más")
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.include_mr_white),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Mr. White no conoce la palabra pero intenta adivinarla",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Switch(
                    checked = mpViewModel.includeMrWhite,
                    onCheckedChange = { mpViewModel.includeMrWhite = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            val canStart = mpViewModel.canStartGame()

            ButtonWithLoading(
                text = stringResource(R.string.begin),
                isLoading = mpViewModel.isLoading,
                enabled = canStart
            ) {
                mpViewModel.startGame(context, spanish)
            }

            if (!canStart) {
                Text(
                    text = "No se puede iniciar la partida: faltan jugadores o roles.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            Text(
                text = stringResource(R.string.waiting_for_host),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}