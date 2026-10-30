package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(private val repo: LavaRepository) : ViewModel() {
    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun alternar(servicio: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(servicio.id, !servicio.activo) }
    }

    fun guardar(servicio: Servicio) {
        viewModelScope.launch {
            if (servicio.nombre.isBlank() || servicio.precio <= 0) {
                _mensaje.value = "Ingresa un nombre y un precio válido"
                return@launch
            }
            repo.guardarServicio(servicio)
        }
    }

    fun eliminar(servicio: Servicio) {
        viewModelScope.launch {
            if (repo.servicioTieneDetalles(servicio.id)) {
                _mensaje.value = "Este servicio ya se usó en pedidos; solo puedes desactivarlo"
                return@launch
            }
            repo.eliminarServicio(servicio)
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }
}
