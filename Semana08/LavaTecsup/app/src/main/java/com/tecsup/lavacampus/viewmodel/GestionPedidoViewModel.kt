package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GestionPedidoViewModel(
    private val repo: LavaRepository,
    pedidoId: Int
) : ViewModel() {
    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val casillerosOcupados: StateFlow<List<String>> = repo.observarCasillerosOcupados()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _casilleroElegido = MutableStateFlow<String?>(null)
    val casilleroElegido: StateFlow<String?> = _casilleroElegido.asStateFlow()
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        viewModelScope.launch {
            repo.observarPedido(pedidoId).filterNotNull().collect { observado ->
                if (_casilleroElegido.value == null) _casilleroElegido.value = observado.pedido.casillero
            }
        }
    }

    fun elegirCasillero(casillero: String) {
        if (casillero !in casillerosOcupados.value || casillero == pedido.value?.pedido?.casillero) {
            _casilleroElegido.value = casillero
        }
    }

    fun marcarPagado(pagado: Boolean) {
        viewModelScope.launch {
            val id = pedido.value?.pedido?.id ?: return@launch
            repo.marcarPagado(id, pagado)
        }
    }

    fun avanzar() {
        viewModelScope.launch {
            val actual = pedido.value ?: return@launch
            val estado = EstadoPedido.valueOf(actual.pedido.estado)
            val siguiente = estado.siguiente()
            if (siguiente == null) {
                _mensaje.value = "Este pedido ya está en un estado final"
                return@launch
            }
            if (siguiente == EstadoPedido.LISTO) {
                val casillero = casilleroElegido.value
                if (casillero == null) {
                    _mensaje.value = "Elige un casillero para marcarlo como listo"
                    return@launch
                }
                if (!esCasilleroValido(casillero)) {
                    _mensaje.value = "El casillero seleccionado no está disponible"
                    return@launch
                }
            }
            if (siguiente == EstadoPedido.ENTREGADO && !actual.pedido.pagado) {
                _mensaje.value = "Debes marcar el pedido como Cobrado antes de entregarlo"
                return@launch
            }
            repo.cambiarEstado(actual.pedido.id, siguiente, casilleroElegido.value)
            _mensaje.value = null
        }
    }

    fun cancelar() {
        viewModelScope.launch {
            val actual = pedido.value ?: return@launch
            val estado = EstadoPedido.valueOf(actual.pedido.estado)
            if (estado !in listOf(EstadoPedido.RECIBIDO, EstadoPedido.EN_PROCESO)) {
                _mensaje.value = "Solo se cancelan pedidos recibidos o en proceso"
                return@launch
            }
            repo.cambiarEstado(actual.pedido.id, EstadoPedido.CANCELADO, null)
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }

    private fun esCasilleroValido(casillero: String): Boolean =
        Regex("C-(0[1-9]|[12][0-9]|30)").matches(casillero) &&
            (casillero !in casillerosOcupados.value || casillero == pedido.value?.pedido?.casillero)
}
