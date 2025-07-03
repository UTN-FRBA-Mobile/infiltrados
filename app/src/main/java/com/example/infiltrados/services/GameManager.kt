package com.example.infiltrados.services

import com.example.infiltrados.models.Player
import com.example.infiltrados.models.Role

class GameManager(
    private val playerNames: List<String>,
    private val wordPair: Pair<String, String>, // Ej: ("gato", "tigre")
    private val numUndercover: Int = 1,
    private val includeMrWhite: Boolean = true,
    var lastEliminated: Player? = null
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
            else -> "" // No debería suceder
        }
    }

    fun getActivePlayers(): List<Player> {
        return players.filter { !it.isEliminated }
    }

    fun getUndercoverActivePlayers(): List<Player> {
        return players.filter { it.role == Role.UNDERCOVER && !it.isEliminated }
    }

    fun getCitizensActivePlayers(): List<Player> {
        return players.filter { it.role == Role.CIUDADANO && !it.isEliminated }
    }

    fun eliminatePlayer(player: Player?) {
        player?.isEliminated = true
    }

    fun isMrWhiteGuessCorrect(word: String): Boolean {
        return word == wordPair.first
    }

    fun isMrWhiteActive(): Boolean {
        return players.any { it.role == Role.MR_WHITE && !it.isEliminated }
    }

    fun mrWhiteWin() {
        players.forEach { it.isEliminated = true }
        players.find { it.role == Role.MR_WHITE }?.isEliminated = false
    }

    fun isGameOver(): Boolean {
        val activePlayers = getActivePlayers()
        val activeRoles = activePlayers.map { it.role }

        val mrWhiteAlive = activeRoles.contains(Role.MR_WHITE)
        val undercoverAlive = activeRoles.contains(Role.UNDERCOVER)
        val citizensAlive = activeRoles.contains(Role.CIUDADANO)

        if (activePlayers.size == 1 && activeRoles.contains(Role.MR_WHITE)) return true


        if (undercoverAlive && !mrWhiteAlive && !citizensAlive) return true


        if (!undercoverAlive && !mrWhiteAlive && citizensAlive) return true

        return false
    }


    fun getWinners(): String {
        val activePlayers = getActivePlayers()
        val activeRoles = activePlayers.map { it.role }

        val mrWhiteAlive = activeRoles.contains(Role.MR_WHITE)
        val undercoverAlive = activeRoles.contains(Role.UNDERCOVER)
        val citizensAlive = activeRoles.contains(Role.CIUDADANO)

        return when {
            activePlayers.size == 1 && activeRoles.contains(Role.MR_WHITE) -> Role.MR_WHITE.toString()
            undercoverAlive && !mrWhiteAlive && !citizensAlive -> Role.UNDERCOVER.toString()
            !undercoverAlive && !mrWhiteAlive && citizensAlive -> Role.CIUDADANO.toString()
            else -> ""
        }
    }


}
