package com.example.infiltrados.ui.main.multiplayerLobbyScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.infiltrados.R
import com.example.infiltrados.ui.main.MultiplayerGameViewModel
import com.example.infiltrados.ui.main.components.ButtonWithLoading
import com.example.infiltrados.ui.main.components.ServerTestPanel


@Composable
fun OnlineLobbyScreen(
    multiplayerGameViewModel: MultiplayerGameViewModel,
    onStartGame: () -> Unit,
    onBackToLobby: () -> Unit
) {
    if (multiplayerGameViewModel.gameManager == null && !multiplayerGameViewModel.isLoading) {
        onBackToLobby()
        return
    }

    if (multiplayerGameViewModel.isLoading) {
        CircularProgressIndicator()
        return
    }

    var gameManager = multiplayerGameViewModel.gameManager!!


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.players_label), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))
        PlayerList(gameManager.getPlayers(), gameManager.isHost) { gameManager.kickPlayer(it) }

        ServerTestPanel()
        Spacer(Modifier.weight(1f))
        ButtonWithLoading(
            stringResource(R.string.begin),
            multiplayerGameViewModel.isLoading,
            onStartGame
        )
    }

}


//@Preview
//@Composable
//fun LobbyScreenPreview() {
//    OnlineLobbyScreen(MultiplayerGameViewModel(),{}, {})
//}