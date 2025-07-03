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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.infiltrados.R
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.components.ButtonWithLoading
import com.example.infiltrados.ui.main.multiplayer.MultiplayerGameViewModel
import com.example.infiltrados.ui.main.multiplayer.ObserveMultiplayerPhase


@Composable
fun OnlineLobbyScreen(
    mpViewModel: MultiplayerGameViewModel,
    onBackToLobby: () -> Unit,
    onNavigateToPhase: (MultiplayerPhase) -> Unit
) {
    if (mpViewModel.gameManager == null && !mpViewModel.isLoading) {
        onBackToLobby()
        return
    }

    if (mpViewModel.isLoading) {
        CircularProgressIndicator()
        return
    }

    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)


    val gameRecord by mpViewModel.game.collectAsState()
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
        PlayerList(players, mpViewModel.isHost) { mpViewModel.removePlayer(it) }

        Spacer(Modifier.height(24.dp))
        //ServerTestPanel(multiplayerGameViewModel)
        Spacer(Modifier.weight(1f))
        if (mpViewModel.isHost) {
            ButtonWithLoading(
                stringResource(R.string.begin),
                mpViewModel.isLoading
            ) { mpViewModel.startGame() }
        } else {
            Text(
                stringResource(R.string.waiting_for_host),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}

//@Preview
//@Composable
//fun LobbyScreenPreview() {
//    OnlineLobbyScreen(MultiplayerGameViewModel(),{}, {})
//}