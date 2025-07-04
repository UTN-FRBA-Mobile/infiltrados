package com.example.infiltrados.ui.main.multiplayer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infiltrados.models.Role
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.components.AnimatedBackground

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun VotationScreen(
    mpViewModel: MultiplayerGameViewModel,
    onNavigateToPhase: (MultiplayerPhase) -> Unit
) {
    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        AnimatedBackground()
        val player = mpViewModel.game.value?.players?.random()
        Column {
            Text(
                text = "Votación en curso",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                modifier = androidx.compose.ui.Modifier.padding(16.dp)
                //TODO: Implemetar votación
            )
            Button(
                onClick = {
                    if (player?.role == Role.MR_WHITE) {
                        mpViewModel.mrWhiteGuess()
                    } else {
                        mpViewModel.eliminatePlayer(player)
                    }
                          },
                modifier = androidx.compose.ui.Modifier.padding(16.dp)
            ) {
                Text(text = "Votar")
            }
        }
    }


}