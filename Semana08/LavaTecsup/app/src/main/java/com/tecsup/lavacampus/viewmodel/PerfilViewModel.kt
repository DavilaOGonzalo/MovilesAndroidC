package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PerfilViewModel(repo: LavaRepository, usuario: Usuario) : ViewModel() {
    private val pedidos = repo.observarPedidosDeUsuario(usuario.id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val activos: StateFlow<Int> = pedidos.map {
        it.count { pedido -> EstadoPedido.valueOf(pedido.pedido.estado) in EstadoPedido.activas }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val totales: StateFlow<Int> = pedidos.map { it.count { pedido -> pedido.pedido.estado != EstadoPedido.CANCELADO.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val kilosLavados: StateFlow<Double> = pedidos.map { lista ->
        lista.filter { it.pedido.estado == EstadoPedido.ENTREGADO.name }
            .flatMap { it.detalles }
            .filter { it.servicio.unidad == "KG" }
            .sumOf { it.detalle.cantidad }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)
}
