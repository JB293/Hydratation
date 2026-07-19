package com.example.hydroflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Thème sombre Material 3 avec accents turquoise personnalisés
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00E5FF),      // Turquoise vif
    onPrimary = Color(0xFF121212),    // Noir contrasté
    primaryContainer = Color(0xFF005b66),
    onPrimaryContainer = Color(0xFFe0f7fa),
    secondary = Color(0xFF26c6da),
    onSecondary = Color(0xFF00363a),
    background = Color(0xFF121212),   // Fond sombre profond
    onBackground = Color(0xFFE3E2E6), // Blanc cassé
    surface = Color(0xFF1E1F22),      // Surface grise Material 3
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF2F3036),
    onSurfaceVariant = Color(0xFFC5C6D0),
    error = Color(0xFFFFB4AB),        // Rouge Material 3 pour l'erreur (Reset)
    onError = Color(0xFF690005)
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00838f),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFFFDFBFF),
    surface = Color(0xFFFDFBFF)
)

@Composable
fun HydrationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Thème sombre par défaut
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Configuration de la typographie système
        content = content
    )
}