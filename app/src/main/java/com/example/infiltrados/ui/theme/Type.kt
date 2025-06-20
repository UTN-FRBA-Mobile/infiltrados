package com.example.infiltrados.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.infiltrados.R

// Fuente para texto (Fredoka)
val bodyFontFamily = FontFamily(
    Font(R.font.fredoka, FontWeight.Normal),
    Font(R.font.fredoka, FontWeight.Bold)
)

// Fuente para títulos (Orbitron)
val displayFontFamily = FontFamily(
    Font(R.font.orbitron, FontWeight.Normal)
)

val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = displayFontFamily),
    displayMedium = TextStyle(fontFamily = displayFontFamily),
    displaySmall = TextStyle(fontFamily = displayFontFamily),

    headlineLarge = TextStyle(fontFamily = displayFontFamily),
    headlineMedium = TextStyle(fontFamily = displayFontFamily),
    headlineSmall = TextStyle(fontFamily = displayFontFamily),

    titleLarge = TextStyle(fontFamily = displayFontFamily),
    titleMedium = TextStyle(fontFamily = displayFontFamily),
    titleSmall = TextStyle(fontFamily = displayFontFamily),

    bodyLarge = TextStyle(fontFamily = bodyFontFamily),
    bodyMedium = TextStyle(fontFamily = bodyFontFamily),
    bodySmall = TextStyle(fontFamily = bodyFontFamily),

    labelLarge = TextStyle(fontFamily = bodyFontFamily),
    labelMedium = TextStyle(fontFamily = bodyFontFamily),
    labelSmall = TextStyle(fontFamily = bodyFontFamily),
)
