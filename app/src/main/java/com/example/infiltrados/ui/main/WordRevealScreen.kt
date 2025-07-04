package com.example.infiltrados.ui.main


import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.models.Player
import com.example.infiltrados.services.GameManager
import com.example.infiltrados.ui.main.components.AnimatedBackground
import com.example.infiltrados.ui.main.components.UndercoverButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordRevealScreen(
    navController: NavController,
    players: List<Player>,
    gameManager: GameManager
) {
    val context = LocalContext.current
    val clickSound = remember { MediaPlayer.create(context, R.raw.sonido_boton) }

    var currentIndex by remember { mutableStateOf(0) }
    var revealed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val isLastPlayer = currentIndex == players.lastIndex
    val currentPlayer = players.getOrNull(currentIndex)
    val currentWord = currentPlayer?.let { gameManager.getWordForPlayer(it) }.orEmpty()
    val wordToShow = if (currentWord.isBlank()) "🤫" else currentWord

    val buttonText = when {
        !revealed -> "Mostrar palabra"
        isLastPlayer && revealed -> "¡Comenzar juego!"
        else -> "Siguiente jugador"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        AnimatedBackground()

        currentPlayer?.let { player ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentPlayer.emoji,
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "${currentPlayer.name.replaceFirstChar { it.uppercase() }}, esta es tu palabra",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = revealed,
                    enter = fadeIn(tween(300)) + scaleIn(tween(300)),
                    exit = fadeOut(tween(200)) + scaleOut(tween(200))
                ) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                            .padding(horizontal = 36.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = wordToShow,
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                UndercoverButton(
                    text = buttonText,
                    icon = Icons.Default.ArrowForward,
                    onClick = {
                        clickSound.start()
                        if (!revealed) {
                            revealed = true
                        } else {
                            revealed = false
                            coroutineScope.launch {
                                delay(400)
                                if (currentIndex < players.lastIndex) {
                                    currentIndex++
                                } else {
                                    navController.navigate("discussion") {
                                        popUpTo("reveal") { inclusive = true }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
