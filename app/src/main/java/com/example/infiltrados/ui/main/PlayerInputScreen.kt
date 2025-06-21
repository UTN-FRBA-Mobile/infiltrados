
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.ui.components.GameButton
import com.example.infiltrados.ui.components.GameTitle
import com.example.infiltrados.ui.components.InfiltradosBackground
import com.example.infiltrados.ui.theme.infiltradosTextFieldColors
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerInputScreen(
    navController: NavController,
    onSubmitPlayers: (List<String>, Int, Boolean, Boolean) -> Unit
) {
    var players by remember { mutableStateOf(listOf<Pair<String, Int>>()) }
    var numUndercover by remember { mutableStateOf(1) }
    var includeMrWhite by remember { mutableStateOf(true) }
    var spanish by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    val validPlayers = players.map { it.first.trim() }.filter { it.isNotEmpty() }
    val numCitizens = validPlayers.size - numUndercover - if (includeMrWhite) 1 else 0
    val canStart = validPlayers.size >= 3 && (numUndercover > 0 || includeMrWhite) && numCitizens >= 2

    InfiltradosBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameTitle(
                text = stringResource(R.string.game_setup_title),
                modifier = Modifier
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                LanguageFlag(spanish, true) { spanish = true }
                LanguageFlag(spanish, false) { spanish = false }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                GameTitle(
                    text = stringResource(R.string.players_label),
                    modifier = Modifier
                )

                players.forEachIndexed { index, (name) ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(Modifier.width(16.dp))
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            IconButton(onClick = {
                                players = players.toMutableList().also { it.removeAt(index) }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary)
                            }
                        }
                    }
                }

                GameButton(
                    text = stringResource(R.string.add_player),
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GameTitle(
                    text = stringResource(R.string.undercover_label),
                    modifier = Modifier
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(onClick = { if (numUndercover > 0) numUndercover-- }) {
                        Icon(Icons.Default.Remove, contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary)
                    }
                    Text(numUndercover.toString(), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.tertiary)
                    IconButton(onClick = { numUndercover++ }) {
                        Icon(Icons.Default.Add, contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.include_mr_white), color = MaterialTheme.colorScheme.onBackground)
                Switch(checked = includeMrWhite, onCheckedChange = { includeMrWhite = it })
            }

            GameButton(
                text = stringResource(R.string.start_game),
                onClick = {
                    onSubmitPlayers(validPlayers, numUndercover, includeMrWhite, spanish)
                    navController.navigate("reveal")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = canStart
            )

            if (!canStart) {
                Text(
                    text = stringResource(R.string.invalid_config),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if (newName.isNotBlank()) {
                                    val iconId = Random.nextInt(1, 6)
                                    players = players + (newName.trim() to iconId)
                                    newName = ""
                                    showDialog = false
                                }
                            }
                        ) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    },
                    title = { Text("Nuevo jugador") },
                    text = {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Nombre del jugador") },
                            singleLine = true,
                            colors = infiltradosTextFieldColors()
                        )
                    }
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




