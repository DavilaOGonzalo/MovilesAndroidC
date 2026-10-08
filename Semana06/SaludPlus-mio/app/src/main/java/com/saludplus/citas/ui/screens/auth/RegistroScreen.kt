package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun RegistroScreen(
    onRegistroCorrecto: () -> Unit,
    onIrTerminos: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }
    var errorConfirmar by remember { mutableStateOf<String?>(null) }
    var mensajeGeneral by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(24.dp))

        CampoTexto(
            valor = nombre,
            onValorChange = { nombre = it; errorNombre = null; mensajeGeneral = null },
            etiqueta = "Nombre completo",
            error = errorNombre
        )
        Spacer(modifier = Modifier.height(12.dp))

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
        Spacer(modifier = Modifier.height(12.dp))

        CampoTexto(
            valor = confirmarPassword,
            onValorChange = { confirmarPassword = it; errorConfirmar = null; mensajeGeneral = null },
            etiqueta = "Confirmar contraseña",
            esPassword = true,
            error = errorConfirmar
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onIrTerminos) {
            Text("Ver términos y condiciones")
        }

        mensajeGeneral?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        BotonPrincipal(
            texto = "Registrarme",
            onClick = {
                errorNombre = if (nombre.isBlank()) "Ingrese su nombre" else null
                errorEmail = when {
                    email.isBlank() -> "Ingrese su email"
                    !email.contains("@") -> "Email inválido"
                    else -> null
                }
                errorPassword = when {
                    password.isBlank() -> "Ingrese su contraseña"
                    password.length < 6 -> "Mínimo 6 caracteres"
                    else -> null
                }
                errorConfirmar = when {
                    confirmarPassword.isBlank() -> "Confirme su contraseña"
                    confirmarPassword != password -> "Las contraseñas no coinciden"
                    else -> null
                }

                val esValido = errorNombre == null && errorEmail == null &&
                        errorPassword == null && errorConfirmar == null

                if (esValido) {
                    val registrado = Repositorio.registrarUsuario(nombre, email, password)
                    if (registrado) {
                        onRegistroCorrecto()
                    } else {
                        mensajeGeneral = "El email ya está registrado"
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onRegistroCorrecto) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}
