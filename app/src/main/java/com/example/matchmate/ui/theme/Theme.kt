package com.example.matchmate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MatchMateLightColorScheme = lightColorScheme(
    primary = MatchMateRed,
    secondary = MatchMatePink,
    tertiary = MatchMateLightPink,
    background = MatchMateBackground,
    surface = MatchMateSurface,
    onPrimary = MatchMateOnPrimary,
    onSecondary = MatchMateOnSecondary,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val MatchMateDarkColorScheme = darkColorScheme(
    primary = MatchMateRed,
    secondary = MatchMatePink,
    tertiary = MatchMateLightPink,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = MatchMateOnPrimary,
    onSecondary = MatchMateOnSecondary,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun MatchMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MatchMateDarkColorScheme else MatchMateLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}