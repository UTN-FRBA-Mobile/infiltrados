package com.example.infiltrados.services

import android.content.Context
import android.util.Log
import com.example.infiltrados.models.GameRecord
import io.appwrite.Client
import io.appwrite.enums.ExecutionMethod
import io.appwrite.exceptions.AppwriteException
import io.appwrite.extensions.toJson
import io.appwrite.services.Databases
import io.appwrite.services.Functions
import io.appwrite.services.Realtime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json

object AppwriteService {
    lateinit var client: Client
    lateinit var realtime: Realtime
    lateinit var databases: Databases

    const val databaseId = "682a685b002e2cbbd56b"
    const val gamesColId = "682a68600026de12cf19"
    const val gameFunctionId = "6833d240002e5216579c"
    const val appwriteEndpoint = "https://fra.cloud.appwrite.io/v1"
    const val projectId = "682a5e680005836aa491"

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint(appwriteEndpoint)
            .setProject(projectId)
            .setSelfSigned(true) // For self signed certificates, only use for development

        realtime = Realtime(client)
        databases = Databases(client)

    }

    suspend fun createGame(playerName: String): GameRecord {
        val functions = Functions(client)
        val execution = functions.createExecution(
            functionId = gameFunctionId,
            method = ExecutionMethod.POST,
            path = "create",
            body = mapOf("hostPlayer" to playerName).toJson()
        )

        val gameId = Json.decodeFromString<String>(execution.responseBody)

        Log.d("Appwrite", "Created game with ID: $gameId")
        val game = getGame(gameId)
        // game no deberia ser nunca null.
        // si falla la creacion, estalla antes
        return game!!
    }

    suspend fun joinGame(gameId: String, playerName: String): GameRecord {
        val functions = Functions(client)
        val execution = functions.createExecution(
            functionId = gameFunctionId,
            method = ExecutionMethod.POST,
            path = "join",
            body = mapOf("gameId" to gameId, "player" to playerName).toJson()
        )

        if (execution.responseStatusCode != 200L) {
            throw Exception("Error joining game ${execution.responseBody}")
        }

        Log.d("Appwrite", "Created game with ID: $gameId")
        val game = getGame(gameId)
        // game no deberia ser nunca null.
        return game!!
    }

    suspend fun getGame(id: String): GameRecord? {
        try {
            databases = Databases(client)

            val result = databases.getDocument(
                databaseId = databaseId,
                collectionId = gamesColId,
                documentId = id,
                nestedType = GameRecord::class.java
            )
            Log.d("Appwrite", result.data.toString())
            return result.data
        } catch (e: AppwriteException) {
            Log.d("Appwrite", "Exception while retieving game with id: $id", e)
            return null
        }

    }

    fun subscribe(id: String): Flow<GameRecord> {
        Log.d("Subscribe", "starting callback flow")
        val flow = callbackFlow {
            try {
                val subscription =
                    realtime.subscribe(
                        "databases.$databaseId.collections.$gamesColId.documents.$id",
                        payloadType = GameRecord::class.java,
                        callback = { it ->
                            // Callback will be executed on all account events.
                            Log.d("Realtime", it.payload.toString())
                            trySend(it.payload)
                        })
                awaitClose {
                    Log.d("Realtime", "Closing subscription")
                    subscription.close()
                }
            } catch (e: Exception) {
                Log.e("Realtime", "error", e)
            }
        }
        return flow
    }

    suspend fun updateGame(game: GameRecord): GameRecord {
        Log.d("Appwrite", "Updating game with ID: ${game.id}")
        val updated = databases.updateDocument(
            databaseId,
            gamesColId,
            game.id,
            game.toJson(),
            nestedType = GameRecord::class.java
        )

        return updated.data
    }

    // for integration testing purposes
    suspend fun deleteGame(id: String) {
        databases.deleteDocument(databaseId, gamesColId, id)
    }

}