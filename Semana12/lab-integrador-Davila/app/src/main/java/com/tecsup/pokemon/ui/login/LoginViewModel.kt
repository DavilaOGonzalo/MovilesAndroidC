package com.tecsup.pokemon.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _sesion = MutableStateFlow<Sesion?>(null)
    val sesion: StateFlow<Sesion?> = _sesion.asStateFlow()
    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    private val _ocupado = MutableStateFlow(false)
    val ocupado: StateFlow<Boolean> = _ocupado.asStateFlow()

    init {
        viewModelScope.launch {
            _sesion.value = repository.sesionActual()
            _cargando.value = false
        }
    }

    fun iniciarSesion(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Escribe tu correo y contraseña."
            return
        }
        viewModelScope.launch {
            _ocupado.value = true
            _error.value = null
            repository.iniciarSesion(correo.trim(), clave)
                .onSuccess { _sesion.value = it }
                .onFailure { _error.value = mensajeError(it) }
            _ocupado.value = false
        }
    }

    fun cerrarSesion() {
        repository.cerrarSesion()
        _sesion.value = null
    }

    private fun mensajeError(error: Throwable): String = when {
        error.message?.contains("network", ignoreCase = true) == true -> "No hay conexión. Revisa tu internet e inténtalo de nuevo."
        error.message?.contains("perfil de Firestore", ignoreCase = true) == true -> error.message.orEmpty()
        else -> "No se pudo iniciar sesión. Revisa tus credenciales y vuelve a intentarlo."
    }
}
