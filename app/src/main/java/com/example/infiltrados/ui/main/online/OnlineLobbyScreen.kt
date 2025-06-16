package com.example.infiltrados.ui.main.online

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.services.GameManager
import com.example.infiltrados.ui.main.LanguageFlag
import com.example.infiltrados.ui.main.components.ServerTestPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineLobbyScreen(
    navController: NavController,
    gameManager: GameManager
) {

    val validPlayers = gameManager.players
    val numUndercover = gameManager.numUndercover
    val includeMrWhite = gameManager.includeMrWhite

    val numCitizens = validPlayers.size - numUndercover - if (includeMrWhite) 1 else 0
    val canStart = validPlayers.size >= 3 && (numUndercover > 0 || includeMrWhite) && numCitizens >= 2

    // Topbar con el volver
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.start_game)) },
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

            Text(text = gameManager.code, style = MaterialTheme.typography.headlineLarge)

            // Lista de jugadores
            Text(
                text = stringResource(R.string.players_label),
                style = MaterialTheme.typography.titleMedium
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                validPlayers.forEachIndexed { index, name ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "- " + name.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            //debug
            ServerTestPanel()

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de iniciar juego
            Button(
                onClick = {
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