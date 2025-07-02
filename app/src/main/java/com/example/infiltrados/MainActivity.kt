package com.example.infiltrados

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.infiltrados.services.GameManager
import com.example.infiltrados.services.WordLoader
import com.example.infiltrados.ui.main.Destination
import com.example.infiltrados.ui.main.DiscussionScreen
import com.example.infiltrados.ui.main.EndGameScreen
import com.example.infiltrados.ui.main.LobbyScreen
import com.example.infiltrados.ui.main.MrWhiteGuessScreen
import com.example.infiltrados.ui.main.MultiplayerRoutes
import com.example.infiltrados.ui.main.PlayerEliminatedScreen
import com.example.infiltrados.ui.main.PlayerInputScreen
import com.example.infiltrados.ui.main.RulesScreen
import com.example.infiltrados.ui.main.SplashScreen
import com.example.infiltrados.ui.main.VotationScreen
import com.example.infiltrados.ui.main.WordRevealScreen
import com.example.infiltrados.ui.main.getOnNavigateToPhase
import com.example.infiltrados.ui.main.multiplayer.MultiplayerGameViewModel
import com.example.infiltrados.ui.main.multiplayer.multiplayerLobbyScreen.OnlineLobbyScreen
import com.example.infiltrados.ui.theme.UndercoverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UndercoverTheme { App() }
        }
    }
}

@Composable
private fun App() {
    val navController = rememberNavController()
    var gameManager: GameManager? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    val mpViewModel = viewModel<MultiplayerGameViewModel>()

    NavHost(navController = navController, startDestination = "splash") {
        composable("lobby") {
            LobbyScreen(navController, onCreateMPGame = { name ->
                mpViewModel.createGame(name)
                navController.navigate(route = Destination.OnlineLobby)
            })
        }

        // MP
        navigation<MultiplayerRoutes>(startDestination = Destination.OnlineLobby) {
            val onNavigateToPhase = getOnNavigateToPhase(navController)

            composable<Destination.OnlineLobby> {
                OnlineLobbyScreen(
                    mpViewModel,
                    onBackToLobby = { navController.navigate("lobby") },
                    onNavigateToPhase
                )
            }

            composable<Destination.Reveal> {
                com.example.infiltrados.ui.main.multiplayer.WordRevealScreen(
                    mpViewModel,
                    onNavigateToPhase
                )
            }
        }


        composable("input") {
            PlayerInputScreen(navController) { names, numUndercover, includeMrWhite, spanish ->
                Log.d("DEBUG", "Idioma recibido en callback: $spanish")
                val wordPairs = WordLoader.loadWordPairs(context, spanish)
                val selectedWordPair = wordPairs.random()

                gameManager = GameManager(
                    playerNames = names,
                    wordPair = selectedWordPair.word1 to selectedWordPair.word2,
                    numUndercover = numUndercover,
                    includeMrWhite = includeMrWhite
                )
            }
        }



        composable("reveal") { backStackEntry ->
            WordRevealScreen(
                navController = navController,
                players = gameManager!!.getActivePlayers(),
                gameManager = gameManager!!
            )
        }

        composable("discussion") { backStackEntry ->
            val players = gameManager!!.getActivePlayers()
            DiscussionScreen(
                navController = navController,
                players = players
            )
        }

        composable("vote") { backStackEntry ->
            val players = gameManager!!.getActivePlayers()
            VotationScreen(
                navController = navController,
                players = players,
                gameManager = gameManager!!
            )
        }

        composable("mr_white_guess") { backStackEntry ->
            MrWhiteGuessScreen(
                navController = navController,
                gameManager = gameManager!!
            )
        }

        composable("end_game") {
            EndGameScreen(
                navController = navController,
                gameManager = gameManager!!,
                players = gameManager!!.players
            )
        }

        composable("player_eliminated") {
            PlayerEliminatedScreen(
                navController = navController,
                gameManager = gameManager!!
            )
        }

        composable("rules") {
            RulesScreen(navController)
        }

        composable("splash") {
            SplashScreen(navController)
        }

    }
}