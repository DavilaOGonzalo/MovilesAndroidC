package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.LogoSaludPlus
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSiguiente: (String) -> Unit) {
    LaunchedEffect(Unit) {
        delay(1200)
        onSiguiente(if (Repositorio.usuarioActual != null) Rutas.HOME else Rutas.LOGIN)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(ColorSplash, MaterialTheme.colorScheme.surface)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoSaludPlus(tamano = 88.dp, mostrarNombre = false)
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Clínica SaludPlus",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tu salud, nuestra prioridad",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private val ColorSplash = androidx.compose.ui.graphics.Color(0xFFEAF5FF)
