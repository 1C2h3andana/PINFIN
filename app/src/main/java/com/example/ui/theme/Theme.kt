package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = Navy900,
    primaryContainer = NavyCardLight,
    onPrimaryContainer = CyberCyan,
    secondary = TealAccent,
    onSecondary = Navy900,
    secondaryContainer = Navy700,
    onSecondaryContainer = EmeraldLight,
    tertiary = GoldAccent,
    onTertiary = Navy900,
    background = Navy900,
    onBackground = TextWhite,
    surface = Navy800,
    onSurface = TextWhite,
    surfaceVariant = NavyCard,
    onSurfaceVariant = TextMuted,
    outline = Navy600,
    error = CrimsonDanger,
    onError = TextWhite,
    errorContainer = Color(0xFF450A0A),
    onErrorContainer = CrimsonLight
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = LightSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCFBF1),
    onSecondaryContainer = Color(0xFF115E59),
    tertiary = AmberOrange,
    onTertiary = Color.White,
    background = LightBg,
    onBackground = TextDark,
    surface = LightSurface,
    onSurface = TextDark,
    surfaceVariant = LightCard,
    onSurfaceVariant = TextDarkMuted,
    outline = Color(0xFFCBD5E1),
    error = CrimsonDanger,
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF991B1B)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek midnight fintech dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
