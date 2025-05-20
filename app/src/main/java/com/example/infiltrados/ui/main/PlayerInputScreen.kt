package com.example.infiltrados.ui.main

import androidx.compose.foundation.Image
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R


@Composable
fun PlayerInputScreen(
    navController: NavController,
    onSubmitPlayers: (List<String>, Int, Boolean, Boolean) -> Unit
) {
    var playerNames by remember { mutableStateOf(listOf("")) }
    var numUndercover by remember { mutableStateOf(1) }
    var includeMrWhite by remember { mutableStateOf(true) }
    var spanish by remember { mutableStateOf(true) }

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
            "Ingrese los nombres de los jugadores",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de idioma
        Text("Idioma", style = MaterialTheme.typography.bodyLarge)
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_spain),
                contentDescription = "Español",
                modifier = Modifier
                    .size(48.dp)
                    .border(2.dp, if (spanish) Color.Blue else Color.Transparent, CircleShape)
                    .clickable { spanish = true }
            )
            Image(
                painter = painterResource(id = R.drawable.ic_us),
                contentDescription = "Inglés",
                modifier = Modifier
                    .size(48.dp)
                    .border(2.dp, if (!spanish) Color.Blue else Color.Transparent, CircleShape)
                    .clickable { spanish = false }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de jugadores
        playerNames.forEachIndexed { index, name ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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

        Button(
            onClick = { addPlayer() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar jugador")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Selector de cantidad de Undercover
        Text("Cantidad de Undercover", style = MaterialTheme.typography.bodyLarge)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { if (numUndercover > 0) numUndercover-- }) {
                Icon(Icons.Default.Remove, contentDescription = "Disminuir undercover")
            }
            Text(numUndercover.toString(), modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { numUndercover++ }) {
                Icon(Icons.Default.Add, contentDescription = "Aumentar undercover")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Switch para incluir a Mr. White
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Incluir a Mr. White")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = includeMrWhite,
                onCheckedChange = { includeMrWhite = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de iniciar juego
        Button(
            onClick = {
                val players = playerNames.map { it.trim() }.filter { it.isNotEmpty() }
                val numCitizens = players.size - numUndercover - if (includeMrWhite) 1 else 0
                val validEnemies = numUndercover > 0 || includeMrWhite
                if (players.size >= 3 && validEnemies && numCitizens >= 2) {
                    Log.d("DEBUG", "Idioma seleccionado al enviar: $spanish")
                    onSubmitPlayers(players, numUndercover, includeMrWhite, spanish)
                    navController.navigate("reveal")
                }
            },
            enabled = run {
                val players = playerNames.map { it.trim() }.filter { it.isNotEmpty() }
                val numCitizens = players.size - numUndercover - if (includeMrWhite) 1 else 0
                val validEnemies = numUndercover > 0 || includeMrWhite
                players.size >= 3 && validEnemies && numCitizens >= 2
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Comenzar juego")
        }
    }
}