package com.example.infiltrados.backend

import android.content.Context
import android.util.Log
import io.appwrite.Client
import io.appwrite.enums.ExecutionMethod
import io.appwrite.extensions.toJson
import io.appwrite.services.Databases
import io.appwrite.services.Functions
import io.appwrite.services.Realtime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GameRecord(
    @SerialName("\$id")
    val id: String,
    val players: List<String>,
    val state: String
)

object Appwrite {
    lateinit var client: Client
    lateinit var realtime: Realtime

    const val databaseId = "682a685b002e2cbbd56b"
    const val gamesColId = "682a68600026de12cf19"
    const val gameFunctionId = "6833d240002e5216579c"

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint("https://fra.cloud.appwrite.io/v1")
            .setProject("682a5e680005836aa491")
            .setSelfSigned(true) // For self signed certificates, only use for development

        realtime = Realtime(client)

    }

    suspend fun createGame(playerName: String): GameRecord {
        val functions = Functions(client)
        val response = functions.createExecution(
            functionId = gameFunctionId,
            method = ExecutionMethod.POST,
            path = "create",
            body = mapOf("hostPlayer" to playerName).toJson()
            )

        val game = Json.decodeFromString<GameRecord>(response.responseBody)

        Log.d("Appwrite", game.toString())
        return game
    }

    suspend fun getGame(id: String): GameRecord {
        val databases = Databases(client)

        val result = databases.getDocument(
            databaseId = databaseId,
            collectionId = gamesColId,
            documentId = id,
            nestedType = GameRecord::class.java
        )
        Log.d("Appwrite", result.data.toString())
        return result.data
    }

    fun subscribe(id:String): Flow<GameRecord> {
        Log.d("Subscribe","starting callback flow")
        val flow = callbackFlow {
            try {
                val subscription =
                    realtime.subscribe("databases.$databaseId.collections.$gamesColId.documents.$id", payloadType = GameRecord::class.java, callback = {it ->
                        // Callback will be executed on all account events.
                        Log.d("Realtime", it.payload.toString())
                        trySend(it.payload)
                    })
                awaitClose {
                    Log.d("Realtime","Closing subscription")
                    subscription.close()
                }
            } catch (e: Exception) {
                Log.e("Realtime", "error", e)
            }
        }
        return flow
    }

}

