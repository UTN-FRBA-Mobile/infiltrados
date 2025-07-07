package com.example.infiltrados.ui.main.multiplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.infiltrados.R
import com.example.infiltrados.models.Role
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.components.AnimatedBackground
import com.example.infiltrados.ui.main.components.AnimatedPulsingIcon
import com.example.infiltrados.ui.main.components.TimerButton
import com.example.infiltrados.ui.main.components.UndercoverButton
import kotlinx.coroutines.delay

@Composable
fun MrWhiteGuessScreen(
    mpViewModel: MultiplayerGameViewModel,
    onNavigateToPhase: (MultiplayerPhase) -> Unit
) {
    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)

    if (mpViewModel.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            AnimatedPulsingIcon(
                painter = painterResource(id = R.drawable.ic_logo),
                size = 96.dp
            )
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        AnimatedBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.mr_white_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            var word_guess by remember { mutableStateOf("") }
            val player = mpViewModel.gameManager?.getPlayerFromName()

            if (player?.role == Role.MR_WHITE) {
                Text(
                    text = stringResource(R.string.mr_white_instruction),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                TextField(
                    value = word_guess,
                    onValueChange = { word_guess = it },
                    placeholder = { Text(stringResource(R.string.guess_placeholder)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
                )

                Spacer(modifier = Modifier.height(32.dp))

                UndercoverButton(
                    onClick = {
                        if (mpViewModel.gameManager?.isMrWhiteGuessCorrect(word_guess) == true) {
                            mpViewModel.mrWhiteWin(player)
                        } else {
                            mpViewModel.eliminateMrWhite()
                        }
                    },
                    text = stringResource(R.string.guess_button),
                )
            } else {
                Text(
                    text = stringResource(R.string.mr_white_guess_waiting),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            val seconds = 30
            var secondsRemaining by remember { mutableIntStateOf(seconds) }
            LaunchedEffect(Unit) {
                while (secondsRemaining > 0) {
                    delay(1000L)
                    secondsRemaining--
                }
            }
            TimerButton(seconds,secondsRemaining)

            if (mpViewModel.isHost && secondsRemaining <= 0) {
                mpViewModel.eliminateMrWhite()
            }
        }
    }
}