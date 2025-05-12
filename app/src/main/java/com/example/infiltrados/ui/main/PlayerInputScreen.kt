package com.example.infiltrados.ui.main

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.Switch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PlayerInputScreen(
    navController: NavController,
    onSubmitPlayers: (List<String>, Int, Boolean) -> Unit
) {
    var playerNames by remember { mutableStateOf(listOf("")) }
    var numUndercover by remember { mutableStateOf(1) } // Número de undercovers
    var includeMrWhite by remember { mutableStateOf(true) } // Si incluye a Mr. White

    fun updateName(index: Int, newName: String) {
        playerNames = playerNames.toMutableList().also {
            it[index] = newName
        }
    }

    fun addPlayer() {
        playerNames = playerNames + ""
    }

    fun removePlayer(index: Int) {
        if (playerNames.size > 1) {
            playerNames = playerNames.toMutableList().also {
                it.removeAt(index)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Ingrese los nombres de los jugadores", style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        playerNames.forEachIndexed { index, name ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { updateName(index, it) },
                    placeholder = { Text("Jugador ${index + 1}") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { removePlayer(index) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar jugador")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { addPlayer() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar jugador")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campo para número de undercovers
        OutlinedTextField(
            value = numUndercover.toString(),
            onValueChange = { numUndercover = it.toIntOrNull() ?: 1 }, // Asegurarse de que sea un número válido
            label = { Text("Número de Undercover") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para incluir o no a Mr. White
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Incluir a Mr. White")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = includeMrWhite,
                onCheckedChange = { includeMrWhite = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val players = playerNames.map { it.trim() }.filter { it.isNotEmpty() }
                if (players.size >= 3) {
                    onSubmitPlayers(players, numUndercover, includeMrWhite)
                    navController.navigate("reveal")
                }
            },
            enabled = playerNames.count { it.trim().isNotEmpty() } >= 3,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Comenzar juego")
        }
    }
}
