package com.example.infiltrados.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.infiltrados.R
import com.example.infiltrados.ui.main.components.PickNameDialog
import com.example.infiltrados.ui.main.components.AnimatedBackground
import com.example.infiltrados.ui.main.components.AnimatedPulsingIcon
import com.example.infiltrados.ui.main.components.UndercoverButton

@Composable
fun LobbyScreen(
    navController: NavController,
    onCreateMPGame: (name: String) -> Unit
) {
    var isNameDialogOpen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        AnimatedBackground()

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            AnimatedPulsingIcon(
                painter = painterResource(id = R.drawable.ic_logo),
                size = 96.dp
            )

            Text(
                text = stringResource(R.string.greeting),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            UndercoverButton(
                text = stringResource(R.string.begin),
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { navController.navigate("input") },
                icon = Icons.Default.PlayArrow
            )

            UndercoverButton(
                text = stringResource(R.string.create_online),
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { isNameDialogOpen = true },
                icon = Icons.Default.Cloud
            )

            UndercoverButton(
                text = stringResource(R.string.view_rules),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                onClick = { navController.navigate("rules") },
                icon = Icons.Default.Info
            )
        }
    }

    if (isNameDialogOpen) {
        PickNameDialog(
            onDismissRequest = { isNameDialogOpen = false },
            onConfirmation = { name ->
                isNameDialogOpen = false
                onCreateMPGame(name)
            }
        )
    }
}
