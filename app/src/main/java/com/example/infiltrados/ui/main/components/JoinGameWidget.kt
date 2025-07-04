package com.example.infiltrados.ui.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.infiltrados.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinGameWidget(
    onJoinGame: (gameId: String) -> Unit
) {
    var gameId by remember { mutableStateOf("") }
    var isShowingTextfield by remember { mutableStateOf(false) }
    var isShowingTextFieldIcon by remember { mutableStateOf(false) }
    Row {
        AnimatedVisibility(
            visible = isShowingTextfield,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally()
        ) {
            // TODO Change this to use an actual icon
            TextFieldWithButton(
                value = gameId,
                onValueChange = {
                    if (it.length <= 5) {
                        gameId = it
                        isShowingTextFieldIcon = gameId.length == 5
                    }

                },
                labelText = stringResource(R.string.join_game),
                icon = if (isShowingTextFieldIcon) "✔️" else "",
                buttonText = stringResource(R.string.join_game),
                buttonIcon = Icons.Default.Hub,
                onClick = { onJoinGame(gameId) }
            )
        }
        AnimatedVisibility(
            visible = !isShowingTextfield,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally()
        ) {
            UndercoverButton(
                text = stringResource(R.string.join_game),
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { isShowingTextfield = true },
                icon = Icons.Default.Hub
            )
        }
    }
}


@Preview
@Composable
fun WidgetPreview() {
    JoinGameWidget {}
}