package com.example.infiltrados.ui.theme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Paleta base
val EspiaOscuro = Color(0xFF1E1E2F)
val SuperficieOscura = Color(0xFF2C2C3C)
val BlancoPuro = Color(0xFFFFFFFF)
val GrisOscuroSuave = Color(0xFF1A1A1A)

val AmarilloClave = Color(0xFFFFC107)
val VioletaAccion = Color(0xFF6C5CFF)
val RojoSospecha = Color(0xFFF44336)
val VerdeCivil = Color(0xFF4CAF50)

//  DARK THEME
val UndercoverDarkColorScheme = darkColorScheme(
    primary = AmarilloClave,
    onPrimary = GrisOscuroSuave,
    secondary = VioletaAccion,
    onSecondary = BlancoPuro,
    background = EspiaOscuro,
    onBackground = BlancoPuro,
    surface = SuperficieOscura,
    onSurface = BlancoPuro,
    error = RojoSospecha,
    onError = BlancoPuro
)

// ️ LIGHT THEME
val UndercoverLightColorScheme = lightColorScheme(
    primary = AmarilloClave,
    onPrimary = Color.Black,
    secondary = VioletaAccion,
    onSecondary = BlancoPuro,
    background = Color(0xFFF7F8FA),
    onBackground = Color(0xFF1E1E2F),
    surface = Color.White,
    onSurface = Color.Black,
    error = RojoSospecha,
    onError = BlancoPuro
)

