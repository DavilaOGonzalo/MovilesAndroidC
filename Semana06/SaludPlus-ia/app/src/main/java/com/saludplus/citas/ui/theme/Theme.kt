package com.saludplus.citas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = TealPrimaryDark,
    onPrimary = TealOnPrimaryDark,
    primaryContainer = TealPrimaryContainerDark,
    onPrimaryContainer = TealOnPrimaryContainerDark,
    secondary = BlueGraySecondaryDark,
    onSecondary = TealOnPrimaryDark,
    secondaryContainer = BlueGraySecondaryContainerDark,
    onSecondaryContainer = BlueGrayOnSecondaryContainerDark,
    tertiary = SoftBlueTertiaryDark,
    onTertiary = TealOnPrimaryDark,
    tertiaryContainer = SoftBlueTertiaryContainerDark,
    onTertiaryContainer = SoftBlueOnTertiaryContainerDark,
    background = SaludBackgroundDark,
    onBackground = SaludOnBackgroundDark,
    surface = SaludBackgroundDark,
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
    primary = TealPrimary,
    onPrimary = TealOnPrimary,
    primaryContainer = TealPrimaryContainer,
    onPrimaryContainer = TealOnPrimaryContainer,
    secondary = BlueGraySecondary,
    onSecondary = BlueGrayOnSecondary,
    secondaryContainer = BlueGraySecondaryContainer,
    onSecondaryContainer = BlueGrayOnSecondaryContainer,
    tertiary = SoftBlueTertiary,
    onTertiary = SoftBlueOnTertiary,
    tertiaryContainer = SoftBlueTertiaryContainer,
    onTertiaryContainer = SoftBlueOnTertiaryContainer,
    background = SaludBackgroundLight,
    onBackground = SaludOnBackgroundLight,
    surface = SaludBackgroundLight,
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
