package com.example.infiltrados.backend

import android.content.Context
import android.util.Log

import io.appwrite.Client
import io.appwrite.services.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.wait

object Appwrite {
    lateinit var client: Client
    lateinit var realtime: Realtime

    val databaseId = "682a685b002e2cbbd56b"
    val gamesColId = "682a68600026de12cf19"

    fun init(context: Context) {
        client = Client(context)
            .setEndpoint("https://fra.cloud.appwrite.io/v1")
            .setProject("682a5e680005836aa491")
            .setSelfSigned(true) // For self signed certificates, only use for development

        realtime = Realtime(client)

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
                    realtime.subscribe("databases.$databaseId.collections.$gamesColId.documents.682a6c190001c178935a", payloadType = GameRecord::class.java, callback = {it ->
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

data class GameRecord(
    val code: String,
    val players: List<String>,
    val state: String
)