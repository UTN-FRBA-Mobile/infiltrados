package com.example.infiltrados.ui.theme
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

private val lightScheme = lightColorScheme(
    primary = PurpleDark,
    onPrimary = OnDark,
    secondary = PurpleSecondary,
    onSecondary = OnDark,
    background = Color.White,
    onBackground = OnLight,
    surface = PurpleLight,
    onSurface = OnLight
)

private val darkScheme = darkColorScheme(
    primary = PurpleSecondary,
    onPrimary = OnDark,
    secondary = PurplePrimary,
    onSecondary = OnDark,
    background = PurpleDark,
    onBackground = OnDark,
    surface = PurpleTertiary,
    surfaceVariant = DarkSurfaceVariant,
    onSurface = Color(0xFFEFEAF4)
)


@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun InfiltradosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}


@Composable
fun infiltradosTextFieldColors(): TextFieldColors {
    val isDarkTheme = isSystemInDarkTheme()


    return TextFieldDefaults.colors(
        focusedTextColor = if (isDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary,
        unfocusedTextColor = if (isDarkTheme) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
        focusedContainerColor = if (isDarkTheme) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = if (isDarkTheme) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
