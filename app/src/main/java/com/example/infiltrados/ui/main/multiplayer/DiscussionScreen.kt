package com.example.infiltrados.ui.main.multiplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.infiltrados.R
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.components.AnimatedBackground
import com.example.infiltrados.ui.main.components.UndercoverButton
import com.example.infiltrados.ui.main.components.WaitingForHost

@Composable
fun DiscussionScreen(
    mpViewModel: MultiplayerGameViewModel,
    onNavigateToPhase: (MultiplayerPhase) -> Unit
) {
    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)

    if (mpViewModel.isLoading) {
        CircularProgressIndicator()
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ){
        AnimatedBackground()

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.discussion_prompt),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
                text = stringResource(R.string.discussion_instruction),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val players = mpViewModel.gameManager?.getActivePlayers() ?: emptyList()

            players.forEachIndexed { index, player ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = "${index + 1}.",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${player.emoji} ${player.name.replaceFirstChar { it.uppercase() }}",
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (mpViewModel.isHost){
                UndercoverButton(
                    text = stringResource(R.string.start_voting),
                    icon = Icons.Default.HowToVote,
                    onClick = { mpViewModel.startVoting() }
                )
            }
            else {
                WaitingForHost()
            }

        }
    }

}