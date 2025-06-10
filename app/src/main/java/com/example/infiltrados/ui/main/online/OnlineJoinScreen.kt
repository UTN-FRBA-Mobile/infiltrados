package com.example.infiltrados.ui.main.online

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.services.GameManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineJoinScreen(
    navController: NavController,
    setGameManager: (GameManager) -> Unit
)
{
    // Topbar con el volver
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.join_game)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.rules_back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            var gameId = remember { mutableStateOf("") }
            var playerName = remember { mutableStateOf("") }

            TextField(
                value = gameId.value,
                onValueChange = { gameId.value = it },
                label = { Text("Join game") },
                placeholder = { Text("game Id") },
                singleLine = true,
            )
            TextField(
                value = playerName.value,
                onValueChange = { playerName.value = it },
                label = { Text("Player Name") },
                singleLine = true,
            )
            Button(
                onClick = {
                    //POST Jugador a juego

                    // Obtener el game manager
                    val gameManager = GameManager(
                        playerNames = listOf(playerName.value),
                        wordPair = "" to "",
                        numUndercover = 1,
                        includeMrWhite = true,
                        code = gameId.value
                    )

                    //
                    setGameManager(gameManager)

                    navController.navigate("online_lobby")
                }
            ) {
                Text("Join Game")
            }
        }

        // Se puede agregar lo del avatar o foto del jugador

    }
}
