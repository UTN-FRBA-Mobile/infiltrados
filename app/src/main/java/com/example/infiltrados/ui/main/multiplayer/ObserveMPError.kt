package com.example.infiltrados.ui.main.multiplayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle

@Composable
fun ObserveMultiplayerError(
    multiplayerGameViewModel: MultiplayerGameViewModel,
    onError: (String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(multiplayerGameViewModel.error, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            multiplayerGameViewModel.error.collect(onError)
        }
    }
}