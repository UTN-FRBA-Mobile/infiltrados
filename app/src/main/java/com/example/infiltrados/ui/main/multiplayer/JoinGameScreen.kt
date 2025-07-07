package com.example.infiltrados.ui.main.multiplayer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.ui.main.components.DisabledButton
import com.example.infiltrados.ui.main.components.PickNameDialog
import com.example.infiltrados.ui.main.components.UndercoverButton

@Composable
fun JoinGameScreen(
    navController: NavController,
    onJoinMPGame: (String, String) -> Unit,
    prefilledCode: String = ""
) {
    val context = LocalContext.current
    var gameCode by rememberSaveable { mutableStateOf("") }
    var showNameDialog by remember { mutableStateOf(false) }

    // 👉 Si viene el código escaneado, lo cargamos automáticamente
    LaunchedEffect(prefilledCode) {
        if (prefilledCode.isNotBlank()) {
            Log.d("JOIN_DEBUG", "Código recibido desde QR: $prefilledCode")
            gameCode = prefilledCode
            showNameDialog = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Icon(
                imageVector = Icons.Default.Login,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.join_game_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = gameCode,
                onValueChange = { gameCode = it },
                label = { Text(stringResource(R.string.enter_game_code)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            DisabledButton(
                text = stringResource(R.string.join),
                icon = Icons.Default.Check,
                onClick = { showNameDialog = true },
                enabled = gameCode.isNotBlank()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.or_scan_qr),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            UndercoverButton(
                onClick = { navController.navigate("qr_scanner") },
                icon = Icons.Default.QrCodeScanner,
                text = stringResource(R.string.scan_qr_code)
            )
        }

        // 👉 Diálogo para ingresar nombre después del QR o botón
        if (showNameDialog) {
            PickNameDialog(
                onDismissRequest = { showNameDialog = false },
                onConfirmation = { name ->
                    showNameDialog = false
                    onJoinMPGame(gameCode.trim(), name.trim())
                }
            )
        }
    }
}
