package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.ClienteResumen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ClientesViewModel(repo: LavaRepository) : ViewModel() {
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()
    val clientes: StateFlow<List<ClienteResumen>> = combine(repo.observarClientes(), _busqueda) { lista, texto ->
        val consulta = texto.trim().lowercase()
        if (consulta.isBlank()) lista else lista.filter {
            it.nombre.lowercase().contains(consulta) ||
                it.codigo.lowercase().contains(consulta) ||
                it.correo.lowercase().contains(consulta)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun cambiarBusqueda(valor: String) { _busqueda.value = valor }
}
