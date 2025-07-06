package com.example.infiltrados.ui.main.multiplayer.multiplayerLobbyScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infiltrados.ui.theme.UndercoverTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerList(players: List<String>, canRemove: Boolean = true, onRemove: (String) -> Unit) {
    Column(Modifier.background(color = MaterialTheme.colorScheme.background)) {
        players.forEachIndexed { index, name ->
            PlayerRow(index, name, canRemove, onClick = { onRemove(name) })
        }
    }
}

@Composable
fun PlayerRow(index: Int, name: String, canRemove: Boolean, onClick: () -> Unit) {
    val avatars = remember {
        listOf(
            "😎",
            "🕵️",
            "👽",
            "🤖",
            "🐵",
            "🐸",
            "🦊",
            "🐼",
            "🐯",
            "👻",
            "🎃",
            "👾",
            "🧠",
            "🐶",
            "🐱"
        ).shuffled()
    }
    // TODO: make a confirm dialog before removing a player
    Surface(
        shape = RectangleShape,
        onClick = { if (canRemove) onClick() },
        contentColor = MaterialTheme.colorScheme.onBackground,
        //border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .minimumInteractiveComponentSize()
        ) {
            Text(avatars[index], fontSize = 20.sp)
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            if (canRemove)
                Icon(Icons.Filled.Close, contentDescription = "Remove")
        }
    }
}


@Preview
@Composable
fun PlayerListPreview() {
    val players = remember {
        mutableListOf(
            "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve", "diez"
        )
    }
    UndercoverTheme {
        PlayerList(players) {
            players.remove(it)
        }
    }
}