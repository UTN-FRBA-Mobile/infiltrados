package com.example.infiltrados.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.infiltrados.ui.main.ServerTestViewModel

@Composable
fun ServerTestPanel() {
    val viewModel = viewModel<ServerTestViewModel>()
    val gameRecord = viewModel.game.collectAsState()
    val gameId = remember { mutableStateOf("1voVj") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 32.dp)
    ) {
        TextField(
            value = gameId.value,
            onValueChange = { gameId.value = it },
            label = { Text("Join game") },
            placeholder = { Text("game Id") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .shadow(4.dp, RoundedCornerShape(12.dp))
        )

        GameButton(
            text = "Query API",
            onClick = { viewModel.getAppwriteGame(gameId.value) }
        )

        GameButton(
            text = "Live",
            onClick = { viewModel.getLiveGame(gameId.value) }
        )

        GameButton(
            text = "Create Game",
            onClick = { viewModel.createGame() }
        )

        Text(
            text = gameRecord.value.toString(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
