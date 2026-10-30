package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetallePedidoViewModel(
    private val repo: LavaRepository,
    pedidoId: Int
) : ViewModel() {
    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val puedeCancelar: StateFlow<Boolean> = pedido.map {
        it?.pedido?.estado == EstadoPedido.RECIBIDO.name
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun cancelar() {
        viewModelScope.launch {
            if (puedeCancelar.value) {
                repo.cambiarEstado(pedido.value?.pedido?.id ?: return@launch, EstadoPedido.CANCELADO, null)
            }
        }
    }
}
