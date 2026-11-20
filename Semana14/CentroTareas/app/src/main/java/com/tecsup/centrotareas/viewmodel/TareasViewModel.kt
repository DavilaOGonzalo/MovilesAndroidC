package com.tecsup.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import com.tecsup.centrotareas.data.TareasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje = _mensaje.asStateFlow()
    fun tareaLargaMala() { _mensaje.value = "Trabajando..."; Thread.sleep(4_000); _mensaje.value = "Terminó" }
}
