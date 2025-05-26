package com.example.infiltrados.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R

@OptIn(ExperimentalMaterial3Api::class)
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

    val validPlayers = playerNames.map { it.trim() }.filter { it.isNotEmpty() }
    val numCitizens = validPlayers.size - numUndercover - if (includeMrWhite) 1 else 0
    val canStart = validPlayers.size >= 3 && (numUndercover > 0 || includeMrWhite) && numCitizens >= 2

    // Topbar con el volver
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.game_setup_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.rules_back))
                    }
                }
            )
        }
    ) { innerPadding ->
        // Para tener scroll vertical si hay muchos jugadores
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Selector de idioma
            Text(text = stringResource(R.string.language_label), style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                LanguageFlag(spanish, true) { spanish = true }
                LanguageFlag(spanish, false) { spanish = false }
            }
            // Lista de jugadores
            Text(text = stringResource(R.string.players_label), style = MaterialTheme.typography.titleMedium)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                playerNames.forEachIndexed { index, name ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { updateName(index, it) },
                            label = { Text(stringResource(R.string.player_hint, index + 1)) },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        IconButton(onClick = { removePlayer(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.rules_back))
                        }
                    }
                }

                OutlinedButton(
                    onClick = { addPlayer() },
                    modifier = Modifier.align(Alignment.Start),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.add_player))
                }
            }
            // Selector de cantidad de Undercover
            Text(text = stringResource(R.string.undercover_label), style = MaterialTheme.typography.titleMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = { if (numUndercover > 0) numUndercover-- }) {
                    Icon(Icons.Default.Remove, contentDescription = null)
                }
                Text(numUndercover.toString(), style = MaterialTheme.typography.bodyLarge)
                IconButton(onClick = { numUndercover++ }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
            // Switch para incluir a Mr. White
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.include_mr_white))
                Switch(
                    checked = includeMrWhite,
                    onCheckedChange = { includeMrWhite = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Botón de iniciar juego
            Button(
                onClick = {
                    onSubmitPlayers(validPlayers, numUndercover, includeMrWhite, spanish)
                    navController.navigate("reveal")
                },
                enabled = canStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(stringResource(R.string.start_game))
            }

            if (!canStart) {
                Text(
                    text = stringResource(R.string.invalid_config),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun LanguageFlag(current: Boolean, target: Boolean, onClick: () -> Unit) {
    val imageRes = if (target) R.drawable.ic_spain else R.drawable.ic_us
    val selected = current == target

    Card(
        modifier = Modifier
            .size(56.dp)
            .clickable { onClick() },
        shape = CircleShape,
        border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 6.dp else 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = if (target) "Español" else "English",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
