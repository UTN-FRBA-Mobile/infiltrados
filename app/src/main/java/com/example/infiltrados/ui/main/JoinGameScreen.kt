package com.example.infiltrados.ui.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infiltrados.R
import com.example.infiltrados.ui.components.GameButton
import com.example.infiltrados.ui.components.InfiltradosBackground
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinGameScreen(
    navController: NavController,
    onJoinWithCode: (String) -> Unit,
    onScanQrClick: () -> Unit
) {
    var gameCode by remember { mutableStateOf(TextFieldValue("")) }
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "scale")

    Scaffold(     containerColor = Color.Transparent
    ) { innerPadding ->
        InfiltradosBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp, vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.join_game_title),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.primary,
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )




                OutlinedTextField(
                    value = gameCode,
                    onValueChange = { gameCode = it },
                    label = { Text(stringResource(R.string.join_game_input_label)) },
                    placeholder = { Text(stringResource(R.string.join_game_input_placeholder)) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    ),

                            modifier = Modifier.fillMaxWidth(0.85f)
                )

                GameButton(
                    text = stringResource(R.string.join_game_button),
                    onClick = {
                        pressed = true
                        onJoinWithCode(gameCode.text)
                    },
                    modifier = Modifier.scale(scale)
                )

                GameButton(
                    text = stringResource(R.string.join_game_qr_button),
                    onClick = { onScanQrClick() },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null
                        )
                    }
                )

                GameButton(
                    text = stringResource(R.string.rules_back),
                    onClick = { navController.navigate("lobby") },
                    modifier = Modifier.scale(scale)
                )
            }
        }
    }
}
