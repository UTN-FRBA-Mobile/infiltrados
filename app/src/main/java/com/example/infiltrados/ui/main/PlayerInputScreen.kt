package com.example.infiltrados.ui.main

import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.ui.main.components.AnimatedBackground
import com.example.infiltrados.ui.main.components.DisabledButton
import com.example.infiltrados.ui.main.components.UndercoverButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerInputScreen(
    navController: NavController,
    onSubmitPlayers: (List<String>, Int, Boolean, Boolean) -> Unit
) {
    val context = LocalContext.current
    val clickSound = remember { MediaPlayer.create(context, R.raw.sonido_boton) }

    var playerNames by remember { mutableStateOf(listOf("")) }
    var playerAvatars by remember { mutableStateOf(listOf("😎")) }
    var numUndercover by remember { mutableStateOf(1) }
    var includeMrWhite by remember { mutableStateOf(true) }
    var spanish by remember { mutableStateOf(true) }

    val avatars = listOf("😎", "🕵️", "👽", "🤖", "🐵", "🐸", "🦊", "🐼", "🐯", "👻", "🎃", "👾", "🧠", "🐶", "🐱")

    fun updateName(index: Int, newName: String) {
        playerNames = playerNames.toMutableList().also { it[index] = newName }
    }

    fun addPlayer() {
        clickSound.start()
        playerNames = playerNames + ""
        playerAvatars = playerAvatars + avatars.random()
    }

    fun removePlayer(index: Int) {
        if (playerNames.size > 1) {
            clickSound.start()
            playerNames = playerNames.toMutableList().also { it.removeAt(index) }
            playerAvatars = playerAvatars.toMutableList().also { it.removeAt(index) }
        }
    }

    val validPlayers = playerNames.map { it.trim() }.filter { it.isNotEmpty() }
    val numCitizens = validPlayers.size - numUndercover - if (includeMrWhite) 1 else 0
    val canStart = validPlayers.size >= 3 && (numUndercover > 0 || includeMrWhite) && numCitizens >= 2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        AnimatedBackground()

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.rules_back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.game_setup_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

            }

            // Idioma
            Text(
                text = stringResource(R.string.language_label),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                LanguageFlag(spanish, true) {
                    clickSound.start()
                    spanish = true
                }
                LanguageFlag(spanish, false) {
                    clickSound.start()
                    spanish = false
                }
            }

            // Jugadores
            Text(
                text = stringResource(R.string.players_label),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                playerNames.forEachIndexed { index, name ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { updateName(index, it) },
                            label = { Text(stringResource(R.string.player_hint, index + 1)) },
                            leadingIcon = {
                                Text(playerAvatars[index], fontSize = 20.sp)
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                focusedTextColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        IconButton(onClick = { removePlayer(index) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    UndercoverButton(
                        text = stringResource(R.string.add_player),
                        onClick = { addPlayer() },
                        icon = Icons.Default.PersonAdd
                    )
                }

            }

            // Cantidad de undercovers
            Text(
                text = stringResource(R.string.undercover_label),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    if (numUndercover > 0) {
                        clickSound.start()
                        numUndercover--
                    }
                }) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Menos",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = numUndercover.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = {
                    clickSound.start()
                    numUndercover++
                }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Más",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Switch Mr. White
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.include_mr_white),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = "Mr. White no conoce la palabra pero intenta adivinarla",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }
                Switch(
                    checked = includeMrWhite,
                    onCheckedChange = {
                        clickSound.start()
                        includeMrWhite = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            DisabledButton(
                text = stringResource(R.string.start_game),
                onClick = {
                    clickSound.start()
                    onSubmitPlayers(validPlayers, numUndercover, includeMrWhite, spanish)
                    navController.navigate("reveal")
                },
                icon = Icons.Default.PlayArrow,
                enabled = canStart
            )

            if (!canStart) {
                Text(
                    text = stringResource(R.string.invalid_config),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
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