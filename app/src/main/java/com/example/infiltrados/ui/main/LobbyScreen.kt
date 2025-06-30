package com.example.infiltrados.ui.main

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.infiltrados.R
import com.example.infiltrados.ui.main.components.JoinGameWidget
import com.example.infiltrados.ui.main.components.PickNameDialog


@Composable
fun LobbyScreen(
    navController: NavController,
    onCreateMPGame: (name: String) -> Unit,
    onJoinMPGame: (gameId: String, name: String) -> Unit
) {
    var isNameDialogOpen by remember { mutableStateOf(false) }
    var gameId = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.greeting),
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = {
                navController.navigate("input")
            }
        ) {
            Text(stringResource(R.string.begin))
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { isNameDialogOpen = true }
        ) {
            Text(stringResource(R.string.create_online))
        }
        Spacer(Modifier.height(8.dp))
        JoinGameWidget { gId -> gameId.value = gId }


        if (isNameDialogOpen) {
            PickNameDialog(
                onDismissRequest = { isNameDialogOpen = false },
                onConfirmation = { name ->
                    isNameDialogOpen = false
                    if (gameId.value.isNotEmpty()) {
                        onJoinMPGame(gameId.value, name)
                    } else {
                        onCreateMPGame(name)
                    }
                })
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("rules")
            }
        ) {
            Text(stringResource(R.string.view_rules))
        }
    }

}

@Preview(name = "Lobby Screen - Default View", showBackground = true)
@Composable
fun LobbyScreenPreview_Default() {
    // For previews, you often don't need a fully functional NavController,
    // but rememberNavController can provide a basic one if your Composable relies on it.
    // However, since LobbyScreen itself doesn't observe NavController state for its UI,
    // a simpler fake would also work. For this example, rememberNavController is fine.
    val navController = rememberNavController()

    MaterialTheme {
        LobbyScreen(
            navController = navController,
            onCreateMPGame = { name ->
                println("Preview: onCreateMPGame called with name: $name")
            },
            onJoinMPGame = { gameId, name ->
                println("Preview: onJoinMPGame called with gameId: $gameId, name: $name")
            }
        )
    }
}
