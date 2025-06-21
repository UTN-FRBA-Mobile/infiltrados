package com.example.infiltrados.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.ui.components.InfiltradosBackground
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.infiltrados.R
import com.example.infiltrados.ui.components.GameTitle
import com.example.infiltrados.ui.components.OptionCard

@Composable
fun GameSelectionScreen(navController: NavController) {
    InfiltradosBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Título principal animado o divertido
            GameTitle(
                text = stringResource(R.string.choose_game_mode),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Ilustración o animación divertida
            Image(
                painter = painterResource(id = R.drawable.detective), // ilustración simpática
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .padding(vertical = 8.dp)
            )

            // Card para Jugar en Mismo Dispositivo
            OptionCard(
                title = stringResource(R.string.play_offline),
                description = stringResource(R.string.play_offline_desc),
                icon = Icons.Default.PlayArrow
            ){ navController.navigate("input") }

            // Card para Jugar Online
            OptionCard(
                title = stringResource(R.string.play_online),
                description = stringResource(R.string.play_online_desc),
                icon = Icons.Default.Wifi
            ){ navController.navigate("insertPlayers") }


            Spacer(Modifier.height(24.dp))
        }
    }
}
