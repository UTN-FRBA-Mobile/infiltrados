package com.example.infiltrados.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun UndercoverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        UndercoverDarkColorScheme
    } else {
        UndercoverLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = UndercoverTypography,
        content = content
    )
}
