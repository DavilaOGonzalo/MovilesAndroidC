package com.saludplus.citas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = BlueOnPrimaryDark,
    primaryContainer = BluePrimaryContainerDark,
    onPrimaryContainer = BlueOnPrimaryContainerDark,
    secondary = BlueSecondaryDark,
    onSecondary = BlueOnPrimaryDark,
    secondaryContainer = BlueSecondaryContainerDark,
    onSecondaryContainer = BlueOnSecondaryContainerDark,
    tertiary = CyanTertiaryDark,
    onTertiary = BlueOnPrimaryDark,
    tertiaryContainer = CyanTertiaryContainerDark,
    onTertiaryContainer = CyanOnTertiaryContainerDark,
    background = SaludBackgroundDark,
    onBackground = SaludOnBackgroundDark,
    surface = SaludSurfaceDark,
    onSurface = SaludOnBackgroundDark,
    surfaceVariant = SaludSurfaceVariantDark,
    onSurfaceVariant = SaludOnSurfaceVariantDark,
    outline = SaludOutlineDark,
    outlineVariant = SaludOutlineVariantDark,
    error = SaludErrorDark,
    errorContainer = SaludErrorContainerDark,
    onErrorContainer = SaludOnErrorContainerDark
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = BlueOnPrimary,
    primaryContainer = BluePrimaryContainer,
    onPrimaryContainer = BlueOnPrimaryContainer,
    secondary = BlueSecondary,
    onSecondary = BlueOnSecondary,
    secondaryContainer = BlueSecondaryContainer,
    onSecondaryContainer = BlueOnSecondaryContainer,
    tertiary = CyanTertiary,
    onTertiary = CyanOnTertiary,
    tertiaryContainer = CyanTertiaryContainer,
    onTertiaryContainer = CyanOnTertiaryContainer,
    background = SaludBackgroundLight,
    onBackground = SaludOnBackgroundLight,
    surface = SaludSurfaceLight,
    onSurface = SaludOnBackgroundLight,
    surfaceVariant = SaludSurfaceVariantLight,
    onSurfaceVariant = SaludOnSurfaceVariantLight,
    outline = SaludOutlineLight,
    outlineVariant = SaludOutlineVariantLight,
    error = SaludErrorLight,
    errorContainer = SaludErrorContainerLight,
    onErrorContainer = SaludOnErrorContainerLight
)

@Composable
fun SaludPlusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
