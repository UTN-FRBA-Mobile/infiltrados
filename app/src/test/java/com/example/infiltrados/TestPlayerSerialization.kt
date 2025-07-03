package com.example.infiltrados

import com.example.infiltrados.models.GameRecord
import com.example.infiltrados.models.Player
import com.example.infiltrados.models.Role
import com.example.infiltrados.services.MultiplayerPhase
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.json.Json
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TestPlayerSerialization {
    @Test
    fun playerSerializationTest() {
        val pl = Player("Juan", Role.UNDERCOVER)
        val expected = "\"Juan:UNDERCOVER\""

        val json = Json.encodeToString(pl)
        assertEquals(expected, json)
        val gson = Gson().toJson(pl)
        assertEquals(expected, gson)
    }

    @Test
    fun playerDeserializationTest() {
        val json = "\"Juan:UNDERCOVER\""
        val pl = Json.decodeFromString<Player>(json)
        assertEquals(pl.name, "Juan")
        assertEquals(pl.role, Role.UNDERCOVER)
    }

    @Test
    fun gameRecordSerializationTest() {
        val gr = GameRecord(
            id = "liLjl",
            players = listOf(Player("Juan", Role.UNDERCOVER)),
            phase = MultiplayerPhase.REVEAL,
            word1 = "word",
            word2 = "anotherWord",
            state = ""
        )
        val expectedJson =
            "{\"id\":\"liLjl\",\"players\":[\"Juan:UNDERCOVER\"],\"phase\":\"REVEAL\",\"word1\":\"word\",\"word2\":\"anotherWord\",\"state\":\"\"}"
        val json = Json.encodeToString(gr)
        assertEquals(expectedJson, json)

//        val gson = Gson().toJson(gr)
//        assertEquals(expectedJson, gson)
    }
}