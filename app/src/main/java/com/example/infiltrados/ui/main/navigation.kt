package com.example.infiltrados.ui.main

import androidx.navigation.NavController
import com.example.infiltrados.services.MultiplayerPhase
import kotlinx.serialization.Serializable

@Serializable
object MultiplayerRoutes

sealed interface Destination {
    @Serializable
    data object OnlineLobby : Destination

    @Serializable
    data object Reveal : Destination

    @Serializable
    data object Discussion : Destination

    @Serializable
    data object Vote : Destination

    @Serializable
    data object MrWhiteGuess : Destination

    @Serializable
    data object EndGame : Destination

    @Serializable
    data object PlayerEliminated : Destination
}

fun getOnNavigateToPhase(navController: NavController): (MultiplayerPhase) -> Unit {
    return { phase: MultiplayerPhase ->
        navController.navigate(phase.destination)
    }
}