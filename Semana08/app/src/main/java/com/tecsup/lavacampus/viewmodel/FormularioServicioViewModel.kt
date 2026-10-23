package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FormularioServicioViewModel(
    private val repo: LavaRepository,
    private val servicioId: Int
) : ViewModel() {
    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()
    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion.asStateFlow()
    private val _unidad = MutableStateFlow("KG")
    val unidad: StateFlow<String> = _unidad.asStateFlow()
    private val _precio = MutableStateFlow("")
    val precio: StateFlow<String> = _precio.asStateFlow()
    private val _horas = MutableStateFlow(24)
    val horas: StateFlow<Int> = _horas.asStateFlow()
    private val _icono = MutableStateFlow("wash")
    val icono: StateFlow<String> = _icono.asStateFlow()
    private val _activo = MutableStateFlow(true)
    val activo: StateFlow<Boolean> = _activo.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        if (servicioId > 0) {
            viewModelScope.launch {
                repo.obtenerServicio(servicioId)?.let { cargar(it) }
            }
        }
    }

    private fun cargar(servicio: Servicio) {
        _nombre.value = servicio.nombre
        _descripcion.value = servicio.descripcion
        _unidad.value = servicio.unidad
        _precio.value = servicio.precio.toString()
        _horas.value = servicio.horasEntrega
        _icono.value = servicio.icono
        _activo.value = servicio.activo
    }

    fun cambiarNombre(valor: String) { _nombre.value = valor }
    fun cambiarDescripcion(valor: String) { _descripcion.value = valor }
    fun cambiarUnidad(valor: String) { _unidad.value = valor }
    fun cambiarPrecio(valor: String) { _precio.value = valor.filter { it.isDigit() || it == '.' } }
    fun cambiarHoras(valor: Int) { _horas.value = valor }
    fun cambiarIcono(valor: String) { _icono.value = valor }
    fun cambiarActivo(valor: Boolean) { _activo.value = valor }

    fun guardar(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val precioNumerico = precio.value.toDoubleOrNull()
            if (nombre.value.isBlank() || precioNumerico == null || precioNumerico <= 0) {
                _error.value = "El nombre es obligatorio y el precio debe ser mayor que cero"
                return@launch
            }
            repo.guardarServicio(
                Servicio(
                    id = if (servicioId > 0) servicioId else 0,
                    nombre = nombre.value.trim(),
                    descripcion = descripcion.value.trim(),
                    unidad = unidad.value,
                    precio = precioNumerico,
                    horasEntrega = horas.value,
                    activo = activo.value,
                    icono = icono.value
                )
            )
            onSuccess()
        }
    }

    fun limpiarError() { _error.value = null }
}
