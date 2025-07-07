package com.example.infiltrados.ui.main


import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.models.Player
import com.example.infiltrados.models.Role
import com.example.infiltrados.services.GameManager
import com.example.infiltrados.ui.main.components.AnimatedBackground
import com.example.infiltrados.ui.main.components.DisabledButton
import com.example.infiltrados.ui.main.components.UndercoverTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotationScreen(
    navController: NavController,
    players: List<Player>,
    gameManager: GameManager
) {
    val context = LocalContext.current
    val clickSound = remember { MediaPlayer.create(context, R.raw.sonido_boton) }

    var selectedPlayer by remember { mutableStateOf<Player?>(null) }
    val selectedIndex = players.indexOf(selectedPlayer)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedBackground()
        Scaffold(
            topBar = {
                UndercoverTopBar(
                    navController = navController,
                    title = stringResource(R.string.votation_title)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(24.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.votation_prompt),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.votation_instruction),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    players.forEachIndexed { index, player ->
                        val isSelected = selectedIndex == index

                        val scale = remember { Animatable(1f) }
                        val coroutineScope = rememberCoroutineScope()

                        LaunchedEffect(isSelected) {
                            if (isSelected) {
                                coroutineScope.launch {
                                    scale.animateTo(
                                        targetValue = 1.1f,
                                        animationSpec = tween(durationMillis = 100, easing = LinearOutSlowInEasing)
                                    )
                                    scale.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(durationMillis = 100)
                                    )
                                }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                                .clickable {
                                    clickSound.start()
                                    selectedPlayer = player
                                },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${player.emoji} ${player.name.replaceFirstChar { it.uppercase() }}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                DisabledButton(
                    text = stringResource(R.string.votation_eliminate),
                    icon = Icons.Default.HowToVote,
                    enabled = selectedPlayer != null,
                    onClick = {
                        selectedPlayer?.let { player ->
                            gameManager.lastEliminated = player
                            if (player.role == Role.MR_WHITE) {
                                navController.navigate("mr_white_guess")
                            } else {
                                navController.navigate("player_eliminated")
                            }
                        }
                    }
                )
            }
        }
    }
}

