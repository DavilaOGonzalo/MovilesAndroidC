package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoTexto

@Composable
fun LoginScreen(
    onLoginCorrecto: () -> Unit,
    onIrRegistro: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }
    var mensajeGeneral by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier.padding(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Ingresa para agendar tu cita médica",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(28.dp))

        CampoTexto(
            valor = email,
            onValorChange = { email = it; errorEmail = null; mensajeGeneral = null },
            etiqueta = "Email",
            tipoTeclado = KeyboardType.Email,
            error = errorEmail,
            icono = Icons.Filled.Email
        )
        Spacer(modifier = Modifier.height(12.dp))

        CampoTexto(
            valor = password,
            onValorChange = { password = it; errorPassword = null; mensajeGeneral = null },
            etiqueta = "Contraseña",
            esPassword = true,
            error = errorPassword,
            icono = Icons.Filled.Lock
        )

        mensajeGeneral?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        BotonPrincipal(
            texto = "Ingresar",
            onClick = {
                errorEmail = when {
                    email.isBlank() -> "Ingrese su email"
                    !email.contains("@") -> "Email inválido"
                    else -> null
                }
                errorPassword = if (password.isBlank()) "Ingrese su contraseña" else null

                val esValido = errorEmail == null && errorPassword == null

                if (esValido) {
                    val exito = Repositorio.iniciarSesion(email, password)
                    if (exito) {
                        onLoginCorrecto()
                    } else {
                        mensajeGeneral = "Email o contraseña incorrectos"
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onIrRegistro) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}
