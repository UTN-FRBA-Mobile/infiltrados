package com.example.infiltrados.ui.main.components

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.infiltrados.R

@Composable
fun ButtonWithLoading(text: String, isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text)
        }
    }
}


@Preview
@Composable
fun ButtonWithLoadingPreview() {
    var isLoading by remember { mutableStateOf(false) }
    ButtonWithLoading("click here", isLoading) {
        isLoading = !isLoading
    }
}