package com.tecsup.lavacampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val LavaShapes = Shapes(
    small = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(22.dp)
)

@Composable
fun LavaTheme(
    rol: String? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val admin = rol == "ADMIN"
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = if (admin) VioletaNoche else TurquesaNoche,
            onPrimary = if (admin) VioletaOnNoche else TurquesaOnNoche,
            primaryContainer = if (admin) VioletaContainerNoche else TurquesaContainerNoche,
            onPrimaryContainer = if (admin) VioletaOnContainerNoche else TurquesaOnContainerNoche,
            inversePrimary = if (admin) Violeta else Turquesa,
            secondary = if (admin) SecundarioAdminNoche else SecundarioNoche,
            onSecondary = if (admin) SecundarioAdminOnNoche else SecundarioOnNoche,
            secondaryContainer = if (admin) SecundarioAdminContainerNoche else SecundarioContainerNoche,
            onSecondaryContainer = if (admin) SecundarioAdminOnContainerNoche else SecundarioOnContainerNoche,
            tertiary = TerciarioNoche,
            onTertiary = TerciarioOnNoche,
            tertiaryContainer = TerciarioContainerNoche,
            onTertiaryContainer = TerciarioOnContainerNoche,
            background = if (admin) FondoNocheAdmin else FondoNoche,
            onBackground = if (admin) TextoNocheAdmin else TextoNoche,
            surface = if (admin) SuperficieNocheAdmin else SuperficieNoche,
            onSurface = if (admin) TextoNocheAdmin else TextoNoche,
            surfaceVariant = if (admin) SuperficieVarianteNocheAdmin else SuperficieVarianteNoche,
            onSurfaceVariant = if (admin) TextoSecundarioNocheAdmin else TextoSecundarioNoche,
            inverseSurface = if (admin) TextoNocheAdmin else TextoNoche,
            inverseOnSurface = if (admin) FondoNocheAdmin else FondoNoche,
            error = ErrorNoche,
            onError = ErrorOnNoche,
            errorContainer = ErrorContainerNoche,
            onErrorContainer = ErrorOnContainerNoche,
            outline = if (admin) BordeNocheAdmin else BordeNoche,
            outlineVariant = if (admin) BordeVarianteNocheAdmin else BordeVarianteNoche,
            scrim = Color.Black
        )
    } else {
        lightColorScheme(
            primary = if (admin) Violeta else Turquesa,
            onPrimary = Color.White,
            primaryContainer = if (admin) VioletaContainer else TurquesaContainer,
            onPrimaryContainer = if (admin) Violeta else Turquesa,
            secondaryContainer = if (admin) VioletaContainer else TurquesaContainer,
            tertiary = if (admin) Color(0xFF16A34A) else Color(0xFF0F766E),
            tertiaryContainer = if (admin) Color(0xFFDCFCE7) else Color(0xFFCCFBF1),
            background = if (admin) VioletaFondo else TurquesaFondo,
            surface = Color.White,
            surfaceVariant = if (admin) Color(0xFFF5F3FF) else Color(0xFFE8F9FC)
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = LavaTypography,
        shapes = LavaShapes,
        content = content
    )
}
