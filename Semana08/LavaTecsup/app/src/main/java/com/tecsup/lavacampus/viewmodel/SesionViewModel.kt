package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: LavaRepository,
    private val preferencias: PreferenciasStore
) : ViewModel() {
    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = preferencias.modoOscuro.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        false
    )
    val notificaciones: StateFlow<Boolean> = preferencias.notificaciones.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        true
    )
    val recibirPedidos: StateFlow<Boolean> = preferencias.recibirPedidos.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        true
    )

    init {
        viewModelScope.launch {
            val usuarioId = preferencias.usuarioId.first()
            if (usuarioId >= 0) _usuarioActual.value = repo.obtenerUsuario(usuarioId)
        }
    }

    fun login(correo: String, clave: String, rolSeleccionado: String) {
        viewModelScope.launch {
            _error.value = null
            if (correo.isBlank() || clave.isBlank()) {
                _error.value = "Completa tu correo y contraseña"
                return@launch
            }

            var usuario: Usuario? = null
            for (intento in 0 until 10) {
                usuario = repo.login(correo.trim(), clave)
                if (usuario != null) break
                delay(100)
            }
            val encontrado = usuario
            if (encontrado == null) {
                _error.value = "Correo o contraseña incorrectos"
                return@launch
            }
            if (encontrado.rol != rolSeleccionado) {
                _error.value = "El perfil no coincide con la pestaña elegida"
                return@launch
            }
            _usuarioActual.value = encontrado
            preferencias.guardarSesion(encontrado.rol, encontrado.id)
        }
    }

    fun guardarModoOscuro(valor: Boolean) {
        viewModelScope.launch { preferencias.guardarModoOscuro(valor) }
    }

    fun guardarNotificaciones(valor: Boolean) {
        viewModelScope.launch { preferencias.guardarNotificaciones(valor) }
    }

    fun guardarRecibirPedidos(valor: Boolean) {
        viewModelScope.launch { preferencias.guardarRecibirPedidos(valor) }
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
        viewModelScope.launch {
            preferencias.cerrarSesion()
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
