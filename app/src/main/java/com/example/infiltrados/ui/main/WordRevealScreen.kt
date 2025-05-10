package com.example.infiltrados.ui.main


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.models.Player
import com.example.infiltrados.services.GameManager

@Composable
fun WordRevealScreen(
    navController: NavController,
    players: List<Player>,
    gameManager: GameManager
) {
    // Estado para llevar el índice actual
    var currentIndex by remember { mutableStateOf(0) }
    var revealed by remember { mutableStateOf(false) }

    val currentPlayer = players[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Turno de:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(currentPlayer.name, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        if (!revealed) {
            Button(onClick = { revealed = true }) {
                Text("Mostrar palabra")
            }
        } else {
            val word = gameManager.getWordForPlayer(currentPlayer)
            Text(
                text = if (word.isNotEmpty()) "Tu palabra es:\n$word" else "No tenés palabra, improvisá!",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    revealed = false
                    if (currentIndex < players.size - 1) {
                        currentIndex++
                    } else {
                        println("Todos los jugadores vieron sus roles.")
                        Log.i("GameManager", "Todos los jugadores vieron sus roles.")
                        navController.navigate("discussion")
                    }
                }
            ) {
                Text("Siguiente jugador")
            }
        }
    }
}
