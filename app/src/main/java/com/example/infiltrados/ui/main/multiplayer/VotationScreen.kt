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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infiltrados.models.Player
import com.example.infiltrados.models.Role
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.components.AnimatedBackground
import com.example.infiltrados.ui.main.components.WaitingForHost

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        AnimatedBackground()

        val playerSelected = mpViewModel.gameManager?.getActivePlayers()?.random()
        val player = mpViewModel.gameManager?.getPlayerFromName()

        Column {
            Text(
                text = "Votación en curso",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                modifier = androidx.compose.ui.Modifier.padding(16.dp)
                //TODO: Implemetar votación, rervisar si tiene sentido que haya pantalla de eliminado
            )
            Text(
                text = "Jugador seleccionado: ${playerSelected?.name ?: "Nadie"}",
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                modifier = androidx.compose.ui.Modifier.padding(16.dp)
            )

            if (mpViewModel.isHost) {
                Button(
                    onClick = {
                        if (playerSelected?.role == Role.MR_WHITE) {
                            mpViewModel.mrWhiteGuess()
                        } else {
                            mpViewModel.eliminatePlayer(playerSelected)
                        }
                    },
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                ) {
                    Text(text = "Eliminar")
                }
            } else {
                WaitingForHost()
            }
        }
    }
}