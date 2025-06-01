package com.anje.kelvin.aconting.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.anje.kelvin.aconting.R

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFF44336), // colorPrimary from colors.xml
    onPrimary = Color.White,
    secondary = Color(0xFFFF4081), // colorAccent from colors.xml
    onSecondary = Color.White,
    tertiary = Color(0xFFD32F2F), // colorPrimaryDark from colors.xml
    onTertiary = Color.White,
    background = Color.White,
    onBackground = Color(0xFF212121), // primary_text from colors.xml
    surface = Color.White,
    onSurface = Color(0xFF212121)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF44336),
    onPrimary = Color.Black,
    secondary = Color(0xFFFF4081),
    onSecondary = Color.Black,
    tertiary = Color(0xFFD32F2F),
    onTertiary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF121212),
    onSurface = Color.White
)

@Composable
fun PakitiniTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
