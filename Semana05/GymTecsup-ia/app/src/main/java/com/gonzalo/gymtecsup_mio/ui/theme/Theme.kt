package com.gonzalo.gymtecsup_mio.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LightGreen,
    secondary = LightGreen,
    tertiary = LightGreen,
    background = Color(0xFF0E1513),
    surface = Color(0xFF121816),
    onPrimary = Color(0xFF03362C),
    onSecondary = Color(0xFF03362C),
    onTertiary = Color(0xFF03362C),
    onBackground = Color(0xFFE4EAE7),
    onSurface = Color(0xFFE4EAE7),
    primaryContainer = Color(0xFF0B3D33),
    onPrimaryContainer = LightGreen,
    secondaryContainer = Color(0xFF0B3D33),
    onSecondaryContainer = LightGreen,
    surfaceVariant = Color(0xFF1F2A27),
    onSurfaceVariant = Color(0xFF9AA6A2),
)

private val LightColorScheme = lightColorScheme(
    primary = DarkGreen,
    secondary = DarkGreen,
    tertiary = DarkGreen,
    background = WhiteSurface,
    surface = WhiteSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = InkDark,
    onSurface = InkDark,
    primaryContainer = LightGreen,
    onPrimaryContainer = DarkGreen,
    secondaryContainer = LightGreen,
    onSecondaryContainer = DarkGreen,
    surfaceVariant = LightGray,
    onSurfaceVariant = InkGray,
)

@Composable
fun GymTecsupmioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}