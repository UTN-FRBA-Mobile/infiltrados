package com.example.infiltrados.ui.main.multiplayer.multiplayerLobbyScreen

import android.media.MediaPlayer
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.infiltrados.R
import com.example.infiltrados.services.MultiplayerPhase
import com.example.infiltrados.ui.main.LanguageFlag
import com.example.infiltrados.ui.main.components.AnimatedBackground
import com.example.infiltrados.ui.main.components.AnimatedPulsingIcon
import com.example.infiltrados.ui.main.components.ButtonWithLoading
import com.example.infiltrados.ui.main.components.GameCodeWidget
import com.example.infiltrados.ui.main.components.UndercoverTopBar
import com.example.infiltrados.ui.main.multiplayer.MultiplayerGameViewModel
import com.example.infiltrados.ui.main.multiplayer.ObserveMultiplayerPhase
import com.example.infiltrados.utils.generateQRCodeBitmap
import androidx.navigation.NavController
import com.example.infiltrados.ui.main.components.UndercoverButton

@Composable
fun OnlineLobbyScreen(
    navController: NavController,
    mpViewModel: MultiplayerGameViewModel,
    onBackToLobby: () -> Unit,
    onNavigateToPhase: (MultiplayerPhase) -> Unit
) {
    val context = LocalContext.current
    val gameRecord by mpViewModel.game.collectAsState()
    val players = gameRecord?.players ?: emptyList()
    val clickSound = remember { MediaPlayer.create(context, R.raw.sonido_boton) }

    var showQR by remember { mutableStateOf(false) }

    if (mpViewModel.gameManager == null && !mpViewModel.isLoading) {
        onBackToLobby()
        return
    }

    if (mpViewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            AnimatedPulsingIcon(
                painter = painterResource(id = R.drawable.ic_logo),
                size = 96.dp
            )
        }
        return
    }

    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)

    Scaffold(
        topBar = {
            UndercoverTopBar(
                navController = navController,
                title = stringResource(R.string.game_setup_title),
                onBack = onBackToLobby
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- Lista de jugadores ---
                Text(
                    text = stringResource(R.string.players_label),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(16.dp))

                PlayerList(players.map { it.name }, mpViewModel.isHost) {
                    mpViewModel.removePlayer(it)
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (mpViewModel.isHost) {
                    // Idioma
                    Text(
                        text = stringResource(R.string.language_label),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        LanguageFlag(mpViewModel.spanish, true) {
                            clickSound.start()
                            mpViewModel.spanish = true
                        }
                        LanguageFlag(mpViewModel.spanish, false) {
                            clickSound.start()
                            mpViewModel.spanish = false
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Configuración de cantidad de Undercover ---
                    Text(
                        text = stringResource(R.string.undercover_label),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            if (mpViewModel.numUndercover > 0) {
                                clickSound.start()
                                mpViewModel.numUndercover--
                            }
                        }) {
                            Icon(Icons.Default.Remove, contentDescription = stringResource(R.string.decrease), tint = MaterialTheme.colorScheme.onBackground)
                        }
                        Text(
                            text = mpViewModel.numUndercover.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        IconButton(onClick = {
                            clickSound.start()
                            mpViewModel.numUndercover++
                        }) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.increase), tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }

                    // --- Switch para incluir Mr. White ---
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.include_mr_white),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = stringResource(R.string.mr_white_instruction),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        Switch(
                            checked = mpViewModel.includeMrWhite,
                            onCheckedChange = {
                                clickSound.start()
                                mpViewModel.includeMrWhite = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Botón de iniciar partida ---
                    val canStart = mpViewModel.canStartGame()
                    ButtonWithLoading(
                        text = stringResource(R.string.begin),
                        isLoading = mpViewModel.isLoading,
                        enabled = canStart,
                        onClick = { mpViewModel.startGame(context, mpViewModel.spanish) },
                        icon = Icons.Default.PlayArrow
                    )
                    if (!canStart) {
                        Text(
                            text = stringResource(R.string.cannot_start_message),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- Sección de QR y código de juego ---
                    UndercoverButton(
                        text = if (showQR) stringResource(R.string.hide_qr) else stringResource(R.string.show_qr),
                        onClick = { showQR = !showQR },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        icon = Icons.Default.QrCode,
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (showQR) {
                        gameRecord?.id?.let { gameId ->
                            val qrBitmap = remember(gameId) { generateQRCodeBitmap(gameId) }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.share_game_qr),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )

                                Image(
                                    bitmap = qrBitmap.asImageBitmap(),
                                    contentDescription = stringResource(R.string.qr_code_description),
                                    modifier = Modifier.size(200.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    GameCodeWidget(gameRecord?.id ?: "")
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimatedPulsingIcon(
                            painter = painterResource(id = R.drawable.ic_logo),
                            size = 64.dp
                        )

                        Text(
                            text = stringResource(R.string.waiting_for_host),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            }
        }
    }
}
