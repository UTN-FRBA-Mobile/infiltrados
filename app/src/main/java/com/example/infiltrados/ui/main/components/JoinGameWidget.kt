package com.example.infiltrados.ui.main.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.infiltrados.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinGameWidget(
    onJoinGame: (gameId: String) -> Unit
) {
    var gameId by remember { mutableStateOf("") }
    Row {
        TextField(
            value = gameId,
            onValueChange = { gameId = it },
            label = { Text(stringResource(R.string.join_game)) },
            placeholder = { Text(stringResource(R.string.game_code)) },
            singleLine = true,
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowOutward,
                    contentDescription = stringResource(R.string.join_game)
                )
            },
        )
        Button(
            onClick = { onJoinGame(gameId) },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues()
        ) {
            Text(stringResource(R.string.join))
        }
    }
}


@Preview
@Composable
fun WidgetPreview() {
    JoinGameWidget {}
}