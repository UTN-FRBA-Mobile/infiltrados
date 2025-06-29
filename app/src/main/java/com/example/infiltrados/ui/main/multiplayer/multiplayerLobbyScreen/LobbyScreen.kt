package com.example.infiltrados.ui.main.multiplayer.multiplayerLobbyScreen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.infiltrados.R
import com.example.infiltrados.ui.main.multiplayer.MultiplayerGameViewModel
import com.example.infiltrados.ui.main.components.ButtonWithLoading


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
    val gameRecord by multiplayerGameViewModel.game.collectAsState()
    val players = gameRecord?.players ?: emptyList()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.players_label), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))
        PlayerList(players, gameManager.isHost) { gameManager.kickPlayer(it) }

        Spacer(Modifier.height(24.dp))
        //ServerTestPanel(multiplayerGameViewModel)
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