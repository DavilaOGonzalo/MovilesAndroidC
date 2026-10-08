package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSiguiente: (String) -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        val destino = if (Repositorio.usuarioActual != null) Rutas.HOME else Rutas.LOGIN
        onSiguiente(destino)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Clínica SaludPlus",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "App Paciente",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
