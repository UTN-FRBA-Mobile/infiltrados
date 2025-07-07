package com.example.infiltrados.ui.main.multiplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.infiltrados.R
import com.example.infiltrados.ui.main.components.AnimatedPulsingIcon
import com.example.infiltrados.ui.main.components.DisabledButton
import com.example.infiltrados.ui.main.components.UndercoverButton
import com.example.infiltrados.ui.main.components.WaitingForHost
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun VotationScreen(
    mpViewModel: MultiplayerGameViewModel,
    onNavigateToPhase: (MultiplayerPhase) -> Unit
) {
    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)

    if (mpViewModel.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            AnimatedPulsingIcon(
                painter = painterResource(id = R.drawable.ic_logo),
                size = 96.dp
            )
        }
        return
    }

    EliminationLobby(mpViewModel = mpViewModel) {
        val game = mpViewModel.game.value
        val activePlayers = mpViewModel.gameManager?.getActivePlayers() ?: emptyList()
        val currentPlayer = mpViewModel.gameManager?.getPlayerFromName()

        var selectedPlayer by remember { mutableStateOf<Player?>(null) }
        val currentPlayerName = currentPlayer?.name
        val alreadyVoted = game?.voteBy?.contains(currentPlayerName) == true
        val context = LocalContext.current
        val clickSound = remember { MediaPlayer.create(context, R.raw.sonido_boton) }

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
//n
                if (!alreadyVoted) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        activePlayers
                            .filter { it.name != currentPlayerName }
                            .forEach { player ->
                                val isSelected = selectedPlayer == player

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            clickSound.start()
                                            selectedPlayer = player
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(6.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${player.emoji} ${player.name.replaceFirstChar { it.uppercase() }}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    DisabledButton(
                        text = stringResource(R.string.votation_eliminate),
                        icon = Icons.Default.HowToVote,
                        enabled = selectedPlayer != null,
                        onClick = {
                            selectedPlayer?.let { mpViewModel.voteForPlayer(it.name) }
                        }
                    )
                } else {
                    WaitingForHost()
                }
            }
        }
    }
}
