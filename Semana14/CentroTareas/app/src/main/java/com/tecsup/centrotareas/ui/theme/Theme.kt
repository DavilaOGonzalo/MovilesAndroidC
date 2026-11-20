package com.tecsup.centrotareas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CentroColors = lightColorScheme(primary = Color(0xFF1565C0), secondary = Color(0xFF26A69A))
@Composable fun CentroTareasTheme(content: @Composable () -> Unit) { MaterialTheme(colorScheme = CentroColors, content = content) }
