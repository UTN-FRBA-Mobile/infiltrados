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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.models.Player
import com.example.infiltrados.services.GameManager


@Composable
fun PlayerEliminatedScreen(
    navController: NavController,
    gameManager: GameManager
) {
    var eliminated = gameManager.lastEliminated
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
            text = "Nombre: ${eliminated?.name} \n Rol: ${gameManager.lastEliminated?.role}",
            style = MaterialTheme.typography.titleMedium
        )
        Button(
            onClick = {
                processPlayerElimination(eliminated, gameManager, navController)
            }
        ) {
            Text("Continuar")
        }
    }
}

fun processPlayerElimination(
    selectedPlayer: Player?,
    gameManager: GameManager,
    navController: NavController
) {
    gameManager.eliminatePlayer(selectedPlayer)
    Log.d("VotationScreen", "El jugador ${selectedPlayer?.name} fue eliminado")

    if(gameManager.isGameOver()) {
        Log.d("VotationScreen", "Fin del juego")
        navController.navigate("end_game")
    } else {
        navController.navigate("reveal")
    }

}

