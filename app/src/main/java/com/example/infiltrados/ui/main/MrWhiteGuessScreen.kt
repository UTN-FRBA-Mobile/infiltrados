package com.example.infiltrados.ui.main

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
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
import androidx.navigation.NavController
import com.example.infiltrados.services.GameManager

@Composable
fun MrWhiteGuessScreen(
    navController: NavController,
    gameManager: GameManager
) {
    var word_guess by remember { mutableStateOf("") }
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
                if(gameManager.isMrWhiteGuessCorrect(word_guess)) {
                    Log.d("MrWhiteGuessScreen", "No adivinaste la palabra.")
                } else {
                    Log.d("MrWhiteGuessScreen", "No adivinaste la palabra.")
                }
                navController.navigate("end_game")
            }
        ) {
            Text("Adivinar")
        }
    }
}



