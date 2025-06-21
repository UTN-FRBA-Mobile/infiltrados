package com.example.infiltrados.ui.main

import androidx.compose.runtime.*
import com.airbnb.lottie.compose.*
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.models.Player
import com.example.infiltrados.services.GameManager
import com.example.infiltrados.ui.components.GameButton
import com.example.infiltrados.ui.components.GameTitle
import com.example.infiltrados.ui.components.InfiltradosBackground


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordRevealScreen(
    navController: NavController,
    players: List<Player>,
    gameManager: GameManager
) {
    var currentIndex by remember { mutableStateOf(0) }
    var revealed by remember { mutableStateOf(false) }

    val currentPlayer = players[currentIndex]
    val word = gameManager.getWordForPlayer(currentPlayer)

    // Lottie animation state
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = revealed,
        iterations = 1,
        speed = 1.5f
    )

    // MediaPlayer for sound
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.word_reveal_pop)
    }

    Scaffold(     containerColor = Color.Transparent
    ) { innerPadding ->
        InfiltradosBackground {
            Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    GameTitle(
                        text = stringResource(R.string.word_reveal_prompt, currentPlayer.name),
                        modifier = Modifier
                    )

                    if (!revealed) {
                        GameButton(
                            text = stringResource(R.string.word_reveal_show),
                            onClick = {
                                revealed = true
                                mediaPlayer.start()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                Modifier.background(MaterialTheme.colorScheme.tertiary)
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (word.isNotEmpty()) word else stringResource(R.string.word_reveal_empty),
                                    style = MaterialTheme.typography.headlineLarge,
                                    letterSpacing = 1.5.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        GameButton(
                            text = if (currentIndex < players.lastIndex)
                                stringResource(R.string.word_reveal_next)
                            else
                                stringResource(R.string.discussion_title),
                            onClick = {
                                revealed = false
                                if (currentIndex < players.lastIndex) {
                                    currentIndex++
                                } else {
                                    navController.navigate("discussion")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Overlay confetti animation
                if (revealed && composition != null) {
                    LottieAnimation(
                        composition,
                        progress,
                        modifier = Modifier
                            .matchParentSize()
                            .alpha(0.6f)
                    )
                }
            }
        }
    }
}
