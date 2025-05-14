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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color


@Composable
fun PlayerInputScreen(
    navController: NavController,
    onSubmitPlayers: (List<String>, Int, Boolean) -> Unit
) {
    var playerNames by remember { mutableStateOf(listOf("")) }
    var numUndercover by remember { mutableStateOf(1) }
    var includeMrWhite by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun updateName(index: Int, newName: String) {
        playerNames = playerNames.toMutableList().also { it[index] = newName }
    }
    fun addPlayer() { playerNames = playerNames + "" }
    fun removePlayer(index: Int) {
        if (playerNames.size > 1) {
            playerNames = playerNames.toMutableList().also { it.removeAt(index) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Ingrese los nombres de los jugadores",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))

        // Lista de inputs de nombres
        playerNames.forEachIndexed { idx, name ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { updateName(idx, it) },
                    placeholder = { Text("Jugador ${idx + 1}") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = { removePlayer(idx) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar jugador")
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = { addPlayer() }, Modifier.fillMaxWidth()) {
            Text("Agregar jugador")
        }

        Spacer(Modifier.height(24.dp))

        // Input de número de undercovers
        OutlinedTextField(
            value = numUndercover.toString(),
            onValueChange = { numUndercover = it.toIntOrNull() ?: numUndercover },
            label = { Text("Número de Undercover") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Switch Mr. White
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Incluir a Mr. White")
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = includeMrWhite,
                onCheckedChange = { includeMrWhite = it }
            )
        }

        Spacer(Modifier.height(16.dp))

        // Mostrar error si hay alguno
        errorMessage?.let { msg ->
            Text(
                msg,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        // Botón para comenzar el juego con validación
        val validPlayers = playerNames.map { it.trim() }.filter { it.isNotEmpty() }
        Button(
            onClick = {
                // Recalcular totales
                val total = validPlayers.size
                val mrWhiteCount = if (includeMrWhite) 1 else 0
                val under = numUndercover
                val enemies = under + mrWhiteCount
                val citizens = total - enemies

                // Validaciones
                errorMessage = when {
                    total < 3 ->
                        "Necesitás al menos 3 jugadores."
                    enemies < 1 ->
                        "Debe haber al menos 1 rol enemigo (Undercover o Mr. White)."
                    citizens < 2 ->
                        "Debe haber al menos 2 ciudadanos."
                    enemies >= total ->
                        "Los enemigos no pueden ser igual o más que el total de jugadores."
                    else -> null
                }

                // Si pasó validación, envía datos y navega
                if (errorMessage == null) {
                    onSubmitPlayers(validPlayers, under, includeMrWhite)
                    navController.navigate("reveal")
                }
            },
            enabled = validPlayers.size >= 3,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Comenzar juego")
        }
    }
}
