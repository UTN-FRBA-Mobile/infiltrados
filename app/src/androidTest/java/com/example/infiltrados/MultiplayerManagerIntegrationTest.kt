package com.example.infiltrados

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.infiltrados.services.AppwriteService
import com.example.infiltrados.services.MultiplayerGameManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MultiplayerManagerIntegrationTest {

    @Before // This method will run before each test
    fun setup() {
        AppwriteService.init(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testCreateGame() = runTest {
        val gameManager = MultiplayerGameManager.createGame(
            "testHost",
            CoroutineScope(Dispatchers.IO)
        ).await()

        assertEquals("testHost", gameManager.game.players[0])
        //clean up
        AppwriteService.deleteGame(gameManager.game.id)
    }

    @Test
    fun testUpdateGame() = runTest {
        val gameManager = MultiplayerGameManager.createGame(
            "testHost",
            CoroutineScope(Dispatchers.IO)
        ).await()

        var updated = gameManager.kickPlayer("testHost").await()
        assertEquals(0, updated.players.size)
        delay(1000)
        assertEquals(0, gameManager.game.players.size)

        //clean up
        AppwriteService.deleteGame(gameManager.game.id)
    }


}