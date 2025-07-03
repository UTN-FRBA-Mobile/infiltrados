package com.example.infiltrados.models

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


fun playerToString(player: Player): String {
    return "${player.name}:${player.role}"
}

fun stringToPlayer(str: String): Player {
    val splitted = str.split(":")
    val name = splitted.first()
    val roleStr = splitted.lastOrNull() ?: ""
    val role = if (!roleStr.isEmpty()) Role.valueOf(roleStr) else Role.NADA
    return Player(name, role)
}

object PlayersSerializer : KSerializer<Player> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("com.example.infiltrados.models.Player", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Player) {
        encoder.encodeString(playerToString(value))
    }

    override fun deserialize(decoder: Decoder): Player {
        return stringToPlayer(decoder.decodeString())
    }
}

class PlayerAdapter : TypeAdapter<Player>() {
    override fun write(out: JsonWriter, player: Player) {
        out.value(playerToString(player))
    }

    override fun read(inReader: JsonReader): Player {
        return stringToPlayer(inReader.nextString())
    }
}


// Representa a cada jugador del juego
@Serializable(with = PlayersSerializer::class)
@JsonAdapter(PlayerAdapter::class)
data class Player(
    val name: String,         // Nombre del jugador
    val role: Role,           // Rol asignado (Ciudadano, Undercover, Mr.White)
    var isEliminated: Boolean = false // Indica si el jugador fue eliminado
)

// Posibles roles de los jugadores
enum class Role {
    NADA,
    CIUDADANO,     // Tiene la palabra correcta
    UNDERCOVER,  // Tiene la palabra parecida
    MR_WHITE,     // No tiene palabra, debe improvisar
    ELIMINATED
}

