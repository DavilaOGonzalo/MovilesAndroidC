package com.gonzalo.menulistasperfil.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = PurpleOnPrimary,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = PurpleOnContainer,
    secondary = PurpleSecondary,
    onSecondary = Color.White,
    tertiary = PurpleTertiary,
    onTertiary = Color.White,
    background = BackgroundApp,
    onBackground = TextPrimary,
    surface = SurfaceApp,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantApp,
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFF9488AE),
    error = Color(0xFFB3261E),
)

// La aplicación usa siempre el esquema claro para mantener el fondo claro
// y los tonos morados de la identidad académica.
@Composable
fun MenuListasPerfilTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}