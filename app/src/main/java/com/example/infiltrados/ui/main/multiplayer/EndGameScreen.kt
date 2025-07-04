package com.example.infiltrados.ui.main.multiplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.components.AnimatedBackground

@Composable
fun EndGameScreen(
    mpViewModel: MultiplayerGameViewModel,
    onNavigateToPhase: (MultiplayerPhase) -> Unit
) {
    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ){
        AnimatedBackground()
        Column {
            Text(
                text = "Ganadores: ",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                //TODO Limpiar el juego
                onClick = { onNavigateToPhase(MultiplayerPhase.LOBBY) },
            ) {
                Text("Jugar de nuevo")
            }
        }
    }
}