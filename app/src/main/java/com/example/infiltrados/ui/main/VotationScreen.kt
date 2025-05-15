package com.example.infiltrados.ui.main


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.example.infiltrados.models.Player
import com.example.infiltrados.models.Role
import com.example.infiltrados.services.GameManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotationScreen(
    navController: NavController,
    players: List<Player>,
    gameManager: GameManager
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hora de votar!",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Votá al jugador que crees que es el infiltrado",
            style = MaterialTheme.typography.titleMedium
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {expanded = !expanded},
            modifier = Modifier.padding(8.dp)
        ) {
            TextField(
                value = selectedPlayer?.name ?: "",
                onValueChange = { },
                label = { Text("Seleccionar jugador") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                players.forEach { player ->
                    DropdownMenuItem(
                        text = { Text(player.name) },
                        onClick = {
                            selectedPlayer = player
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if(selectedPlayer != null) {
                    gameManager.lastEliminated = selectedPlayer
                    if(selectedPlayer!!.role == Role.MR_WHITE) {
                        navController.navigate("mr_white_guess")
                    } else {
                        navController.navigate("player_eliminated")
                    }
                }
            }
        ) {
            Text("Eliminar")
        }
    }
}


