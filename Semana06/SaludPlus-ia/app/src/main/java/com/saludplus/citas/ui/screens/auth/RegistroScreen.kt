package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonGradiente
import com.saludplus.citas.ui.components.CampoTexto
import com.saludplus.citas.ui.components.EncabezadoConVolver

@Composable
fun RegistroScreen(
    onRegistroCorrecto: () -> Unit,
    onIrTerminos: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmarVisible by remember { mutableStateOf(false) }
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorEmail by remember { mutableStateOf<String?>(null) }
    var errorPassword by remember { mutableStateOf<String?>(null) }
    var errorConfirmar by remember { mutableStateOf<String?>(null) }
    var mensajeGeneral by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 14.dp)
    ) {
        EncabezadoConVolver(titulo = "Crear cuenta", onVolver = onRegistroCorrecto)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Regístrate para acceder a todos nuestros servicios",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        CampoTexto(
            valor = nombre,
            onValorChange = { nombre = it; errorNombre = null; mensajeGeneral = null },
            etiqueta = "Nombre completo",
            error = errorNombre,
            icono = Icons.Filled.Person
        )
        Spacer(modifier = Modifier.height(10.dp))
        CampoTexto(
            valor = telefono,
            onValorChange = { telefono = it; mensajeGeneral = null },
            etiqueta = "Número telefónico",
            tipoTeclado = KeyboardType.Phone,
            icono = Icons.Filled.Phone
        )
        Spacer(modifier = Modifier.height(10.dp))
        CampoTexto(
            valor = email,
            onValorChange = { email = it; errorEmail = null; mensajeGeneral = null },
            etiqueta = "Correo electrónico",
            tipoTeclado = KeyboardType.Email,
            error = errorEmail,
            icono = Icons.Filled.Email
        )
        Spacer(modifier = Modifier.height(10.dp))
        CampoTexto(
            valor = password,
            onValorChange = { password = it; errorPassword = null; mensajeGeneral = null },
            etiqueta = "Contraseña",
            esPassword = true,
            passwordVisible = passwordVisible,
            onTogglePassword = { passwordVisible = !passwordVisible },
            error = errorPassword,
            icono = Icons.Filled.Lock
        )
        Spacer(modifier = Modifier.height(10.dp))
        CampoTexto(
            valor = confirmarPassword,
            onValorChange = {
                confirmarPassword = it
                errorConfirmar = null
                mensajeGeneral = null
            },
            etiqueta = "Confirmar contraseña",
            esPassword = true,
            passwordVisible = confirmarVisible,
            onTogglePassword = { confirmarVisible = !confirmarVisible },
            error = errorConfirmar,
            icono = Icons.Filled.Lock
        )

        TextButton(onClick = onIrTerminos) {
            Text("Ver términos y condiciones")
        }

        mensajeGeneral?.let {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.errorContainer
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        BotonGradiente(
            texto = "Registrarse",
            onClick = {
                val nombreError = if (nombre.isBlank()) "Ingrese su nombre" else null
                val emailError = when {
                    email.isBlank() -> "Ingrese su email"
                    !email.contains("@") -> "Email inválido"
                    else -> null
                }
                val passwordError = when {
                    password.isBlank() -> "Ingrese su contraseña"
                    password.length < 6 -> "Mínimo 6 caracteres"
                    else -> null
                }
                val confirmarError = when {
                    confirmarPassword.isBlank() -> "Confirme su contraseña"
                    confirmarPassword != password -> "Las contraseñas no coinciden"
                    else -> null
                }
                errorNombre = nombreError
                errorEmail = emailError
                errorPassword = passwordError
                errorConfirmar = confirmarError

                if (nombreError == null && emailError == null &&
                    passwordError == null && confirmarError == null
                ) {
                    if (Repositorio.registrarUsuario(nombre, email, password, telefono)) {
                        onRegistroCorrecto()
                    } else {
                        mensajeGeneral = "El email ya está registrado"
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(onClick = onRegistroCorrecto, modifier = Modifier.fillMaxWidth()) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}
