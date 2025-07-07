package com.example.infiltrados.ui.main.multiplayer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.infiltrados.R
import com.example.infiltrados.ui.main.components.UndercoverButton


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
    ) {
        AnimatedBackground()

        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.votation_prompt),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.votation_instruction),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            if (!alreadyVoted) {
                activePlayers
                    .filter { it.name != currentPlayerName }
                    .forEach { player ->
                        UndercoverButton(
                            text = player.name,
                            onClick = { selectedPlayer = player }
                        )
                    }

                selectedPlayer?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    UndercoverButton(
                        text = stringResource(R.string.votation_confirm_vote) + ":\n${it.name}",
                        onClick = {mpViewModel.voteForPlayer(it.name)}
                    )
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.votation_waiting_vote))
                    CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}