package com.example.infiltrados.ui.main.multiplayer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.models.Player
import com.example.infiltrados.services.GameManager
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.components.AnimatedBackground

@Composable
fun PlayerEliminatedScreen(
    mpViewModel: MultiplayerGameViewModel,
    onNavigateToPhase: (MultiplayerPhase) -> Unit
)
{
    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ){
        AnimatedBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Jugador eliminado!",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Nombre: \n Rol: ",
                style = MaterialTheme.typography.titleMedium
            )
            Button(
                onClick = {
                    if (mpViewModel.gameContinues())
                        mpViewModel.startDiscussion()
                    else
                        mpViewModel.endGame()
                }
            ) {
                Text("Continuar")
            }
        }
    }

}