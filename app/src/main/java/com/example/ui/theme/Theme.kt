package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ActiveBrandRed,
    onPrimary = Color.White,
    primaryContainer = NetflixRed,
    onPrimaryContainer = Color.White,
    secondary = FacebookBlue,
    onSecondary = Color.White,
    tertiary = PrimeAmber,
    onTertiary = Color.Black,
    background = YouTubeBlack,
    onBackground = YouTubeTextPrimary,
    surface = YouTubeBlack,
    onSurface = YouTubeTextPrimary,
    surfaceVariant = YouTubeDarkSurface,
    onSurfaceVariant = YouTubeTextSecondary,
    outline = YouTubeDivider
)

private val LightColorScheme = lightColorScheme(
    primary = ActiveBrandRed,
    onPrimary = Color.White,
    primaryContainer = NetflixRed,
    onPrimaryContainer = Color.White,
    secondary = FacebookBlue,
    onSecondary = Color.White,
    tertiary = PrimeAmber,
    onTertiary = Color.Black,
    background = Color(0xFFF9F9F9),
    onBackground = Color(0xFF0F0F0F),
    surface = Color.White,
    onSurface = Color(0xFF0F0F0F),
    surfaceVariant = Color(0xFFF2F2F2),
    onSurfaceVariant = Color(0xFF606060),
    outline = Color(0xFFE5E5E5)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to YouTube Dark Theme for true YouTube experience
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
