package com.example.infiltrados.ui.main.multiplayer

import androidx.compose.runtime.Composable
import com.example.infiltrados.services.MultiplayerPhase

@Composable
fun WordRevealScreen(
    mpViewModel: MultiplayerGameViewModel,
    onNavigateToPhase: (MultiplayerPhase) -> Unit
) {
    ObserveMultiplayerPhase(mpViewModel, onNavigateToPhase)
}