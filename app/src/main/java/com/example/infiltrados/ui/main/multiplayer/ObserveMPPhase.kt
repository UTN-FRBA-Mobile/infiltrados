package com.example.infiltrados.ui.main.multiplayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.infiltrados.services.MultiplayerPhase

@Composable
fun ObserveMultiplayerPhase(
    multiplayerGameViewModel: MultiplayerGameViewModel,
    onPhaseChanged: (MultiplayerPhase) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(multiplayerGameViewModel.phase, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            multiplayerGameViewModel.phase.collect { phase ->
                onPhaseChanged(phase)
            }
        }
    }
}