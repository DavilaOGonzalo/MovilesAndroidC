package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MisPedidosViewModel(
    private val repo: LavaRepository,
    usuario: Usuario
) : ViewModel() {
    private val pedidos = repo.observarPedidosDeUsuario(usuario.id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val activos: StateFlow<List<PedidoConDetalle>> = pedidos.map { lista ->
        lista.filter { EstadoPedido.valueOf(it.pedido.estado) in EstadoPedido.activas }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val historial: StateFlow<List<PedidoConDetalle>> = pedidos.map { lista ->
        lista.filter { EstadoPedido.valueOf(it.pedido.estado) in listOf(EstadoPedido.ENTREGADO, EstadoPedido.CANCELADO) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun cancelar(pedido: PedidoConDetalle) {
        if (pedido.pedido.estado == EstadoPedido.RECIBIDO.name) {
            viewModelScope.launch {
                repo.cambiarEstado(pedido.pedido.id, EstadoPedido.CANCELADO, null)
            }
        }
    }
}
