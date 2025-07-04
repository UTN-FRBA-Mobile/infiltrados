package com.example.infiltrados.ui.main.multiplayer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infiltrados.models.Role
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.components.AnimatedBackground

@Composable
fun MrWhiteGuessScreen(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Mr White descubierto!",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            var word_guess by remember { mutableStateOf("") }
            val player = mpViewModel.gameManager?.getPlayerFromName()
            if (player?.role== Role.MR_WHITE)
            {
                Text(
                    text = "Adivina la palabra de los civiles",
                    style = MaterialTheme.typography.titleMedium
                )
                TextField(
                    value = word_guess,
                    onValueChange = { word_guess = it },
                    placeholder = { Text("Palabra de civiles") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Button(
                    onClick = {
                        if(mpViewModel.game.value?.word1 == word_guess) {
                            mpViewModel.endGame()
                        } else {
                            mpViewModel.eliminatePlayer(player)
                        }
                    }
                ) {
                    Text("Adivinar")
                }
            }
            else {
                Text(
                    text = "Esperando a que Mr White adivine la palabra.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

        }
    }




}