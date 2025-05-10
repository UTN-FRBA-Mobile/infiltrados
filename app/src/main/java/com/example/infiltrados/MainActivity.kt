package com.example.infiltrados

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.infiltrados.services.GameManager
import com.example.infiltrados.ui.main.DiscussionScreen
import com.example.infiltrados.ui.main.LobbyScreen
import com.example.infiltrados.ui.main.PlayerInputScreen
import com.example.infiltrados.ui.main.VotationScreen
import com.example.infiltrados.ui.main.WordRevealScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
private fun App() {
    val navController = rememberNavController()
    var gameManager: GameManager? by remember { mutableStateOf(null) }

    NavHost(navController = navController, startDestination = "lobby") {

        composable("lobby") { LobbyScreen(navController) }

        composable("input") {
            PlayerInputScreen(navController) { names ->
                gameManager = GameManager(
                    playerNames = names,
                    wordPair = "gato" to "tigre", // Palabras fijas por ahora
                    numUndercover = 1,
                    includeMrWhite = true
                )
            }
        }

        composable("reveal") { backStackEntry ->
            WordRevealScreen(
                navController = navController,
                players = gameManager!!.players,
                gameManager = gameManager!!
            )
        }

        composable("discussion") { backStackEntry ->
            val players = gameManager!!.players
            DiscussionScreen(
                navController = navController,
                players = players
            )
        }

        composable("vote") { backStackEntry ->
            val players = gameManager!!.players
            VotationScreen(
                navController = navController,
                players = players,
                gameManager = gameManager!!
            )
        }


    }
}