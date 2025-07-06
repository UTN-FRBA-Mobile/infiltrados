package com.example.infiltrados.models

import com.example.infiltrados.services.MultiplayerPhase
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class GameRecord(
    @SerializedName("\$id")
    val id: String,
    val players: List<Player>,
    val phase: MultiplayerPhase,
    val word1: String,
    val word2: String,
    val state: String,
    val votes: List<String> = emptyList(),
    val votedBy: List<String> = emptyList()
)