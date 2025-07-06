package com.example.infiltrados.ui.main.components

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun ButtonWithLoading(
    text: String,
    isLoading: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonWithLoadingPreview() {
    var isLoading by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        ButtonWithLoading(
            text = "Click Here",
            isLoading = isLoading,
            enabled = isEnabled
        ) {
            isLoading = !isLoading
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { isEnabled = !isEnabled }) {
            Text("Toggle Enabled (Now: ${if (isEnabled) "Enabled" else "Disabled"})")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { isLoading = !isLoading }) {
            Text("Toggle Loading (Now: ${if (isLoading) "Loading" else "Idle"})")
        }
    }
}
