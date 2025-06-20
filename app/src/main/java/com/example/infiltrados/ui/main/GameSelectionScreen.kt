package com.example.infiltrados.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.ui.components.GameButton
import com.example.infiltrados.ui.components.InfiltradosBackground
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.*
import androidx.compose.ui.res.stringResource
import com.example.infiltrados.R

@Composable
fun GameSelectionScreen(
    onSelectOnline: () -> Unit,
    onSelectOffline: () -> Unit
) {
    var pressedOnline by remember { mutableStateOf(false) }
    var pressedOffline by remember { mutableStateOf(false) }

    val scaleOnline by animateFloatAsState(
        targetValue = if (pressedOnline) 0.97f else 1f,
        label = "onlineScale"
    )

    val scaleOffline by animateFloatAsState(
        targetValue = if (pressedOffline) 0.97f else 1f,
        label = "offlineScale"
    )

    InfiltradosBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            Text(
                text = stringResource(R.string.select_game_mode),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            GameButton(
                text = stringResource(R.string.play_online),
                onClick = {
                    pressedOnline = true
                    onSelectOnline()
                },
                modifier = Modifier.scale(scaleOnline)
            )

            GameButton(
                text = stringResource(R.string.play_offline),
                onClick = {
                    pressedOffline = true
                    onSelectOffline()
                },
                modifier = Modifier.scale(scaleOffline)
            )
        }
    }
}
