package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))

        CampoTexto(
            valor = email,
            onValorChange = { email = it; errorEmail = null; mensajeGeneral = null },
            etiqueta = "Email",
            tipoTeclado = KeyboardType.Email,
            error = errorEmail
        )
        Spacer(modifier = Modifier.height(12.dp))

        CampoTexto(
            valor = password,
            onValorChange = { password = it; errorPassword = null; mensajeGeneral = null },
            etiqueta = "Contraseña",
            esPassword = true,
            error = errorPassword
        )

        mensajeGeneral?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
