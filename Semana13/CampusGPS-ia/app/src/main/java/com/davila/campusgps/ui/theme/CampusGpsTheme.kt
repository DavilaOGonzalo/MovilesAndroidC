package com.davila.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Color(0xFF174A75),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD8E9FF),
    onPrimaryContainer = Color(0xFF082F50),
    secondary = Color(0xFF256FE5),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE8FF),
    onSecondaryContainer = Color(0xFF102F67),
    tertiary = Color(0xFF19836D),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1F4E9),
    onTertiaryContainer = Color(0xFF064D40),
    error = Color(0xFFB42332),
    background = Color(0xFFF3F7FC),
    onBackground = Color(0xFF172B3D),
    surface = Color(0xFFFCFDFF),
    onSurface = Color(0xFF172B3D),
    surfaceVariant = Color(0xFFE9F0F7),
    onSurfaceVariant = Color(0xFF52677B),
    outline = Color(0xFF8193A5),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA9CCFF),
    onPrimary = Color(0xFF07365D),
    primaryContainer = Color(0xFF174A75),
    onPrimaryContainer = Color(0xFFD8E9FF),
    secondary = Color(0xFFA9C6FF),
    onSecondary = Color(0xFF102F67),
    secondaryContainer = Color(0xFF294C88),
    onSecondaryContainer = Color(0xFFDCE8FF),
    tertiary = Color(0xFF8AD7C2),
    onTertiary = Color(0xFF00382E),
    tertiaryContainer = Color(0xFF075848),
    onTertiaryContainer = Color(0xFFD1F4E9),
    error = Color(0xFFFFB4AB),
    background = Color(0xFF0D1B29),
    onBackground = Color(0xFFE0EAF4),
    surface = Color(0xFF142536),
    onSurface = Color(0xFFE0EAF4),
    surfaceVariant = Color(0xFF23384B),
    onSurfaceVariant = Color(0xFFB8C7D7),
    outline = Color(0xFF8297AA),
)

private val CampusTypography = Typography().copy(
    headlineSmall = TextStyle(fontSize = 24.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
    titleSmall = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 23.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
)

private val CampusShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@Composable
fun CampusGpsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = CampusTypography,
        shapes = CampusShapes,
        content = content,
    )
}
