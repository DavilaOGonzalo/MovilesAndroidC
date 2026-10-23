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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class BandejaViewModel(private val repo: LavaRepository) : ViewModel() {
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()
    private val _filtro = MutableStateFlow("Todos")
    val filtro: StateFlow<String> = _filtro.asStateFlow()
    private val atrasados = repo.observarAtrasados(LocalDate.now().toString())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val pedidos: StateFlow<List<PedidoConDetalle>> = combine(
        repo.observarPedidos(),
        _busqueda,
        _filtro,
        atrasados
    ) { todos, texto, filtro, atrasadosIds ->
        val consulta = texto.trim().lowercase()
        todos.filter { pedido ->
            val estado = EstadoPedido.valueOf(pedido.pedido.estado)
            val coincideFiltro = when (filtro) {
                "Recibido" -> estado == EstadoPedido.RECIBIDO
                "En proceso" -> estado == EstadoPedido.EN_PROCESO
                "Listo" -> estado == EstadoPedido.LISTO
                else -> estado in EstadoPedido.activas
            }
            val coincideTexto = consulta.isBlank() ||
                pedido.usuario.nombre.lowercase().contains(consulta) ||
                pedido.pedido.id.toString().contains(consulta)
            coincideFiltro && coincideTexto
        }.map { pedido ->
            pedido to (pedido.pedido.id in atrasadosIds)
        }.sortedWith(compareByDescending<Pair<PedidoConDetalle, Boolean>> { it.second }.thenByDescending { it.first.pedido.id })
            .map { it.first }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val idsAtrasados: StateFlow<Set<Int>> = atrasados
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun cambiarBusqueda(valor: String) { _busqueda.value = valor }
    fun cambiarFiltro(valor: String) { _filtro.value = valor }
}
