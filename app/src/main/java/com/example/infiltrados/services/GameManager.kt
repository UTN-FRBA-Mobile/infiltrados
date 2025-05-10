package com.example.infiltrados.services

import com.example.infiltrados.models.Player
import com.example.infiltrados.models.Role

class GameManager(
    private val playerNames: List<String>,
    private val wordPair: Pair<String, String>, // Ej: ("gato", "tigre")
    private val numUndercover: Int = 1,
    private val includeMrWhite: Boolean = true
) {
    val players: List<Player>

    init {
        // Barajamos los jugadores
        val shuffledNames = playerNames.shuffled()

        // Asignamos roles según la cantidad de jugadores
        val roles = mutableListOf<Role>()

        repeat(numUndercover) { roles.add(Role.UNDERCOVER) }
        if (includeMrWhite) roles.add(Role.MR_WHITE)
        while (roles.size < playerNames.size) roles.add(Role.CIUDADANO)
        roles.shuffle()

        // Creamos la lista de jugadores con sus roles y palabras
        players = shuffledNames.mapIndexed { index, name ->
            Player(
                name = name,
                role = roles[index]
            )
        }
    }

    // Devuelve la palabra asignada al jugador según su rol
    fun getWordForPlayer(player: Player): String {
        return when (player.role) {
            Role.CIUDADANO -> wordPair.first
            Role.UNDERCOVER -> wordPair.second
            Role.MR_WHITE -> "" // No tiene palabra
        }
    }

    fun getActivePlayers(): List<Player> {
        return players.filter { !it.isEliminated }
    }

    fun eliminatePlayer(player: Player) {
        player.isEliminated = true
    }

    fun isMrWhiteGuessCorrect(word: String): Boolean {
        return word == wordPair.first
    }

    fun getWinners(): List<Player> {
        val undercoverPlayers = players.filter { it.role == Role.UNDERCOVER && !it.isEliminated }
        val citizens = players.filter { it.role == Role.CIUDADANO && !it.isEliminated }
        val mrWhites = players.filter { it.role == Role.MR_WHITE && !it.isEliminated }

        return when {
            undercoverPlayers.isEmpty() -> citizens // Ganaron los ciudadanos
            citizens.isEmpty() -> undercoverPlayers // Ganaron los infiltrados
            else -> emptyList() // Nadie ha ganado aún
        }
    }

}
