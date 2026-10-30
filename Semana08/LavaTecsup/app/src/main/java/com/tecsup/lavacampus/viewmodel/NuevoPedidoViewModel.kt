package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.LineaPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.math.roundToInt

class NuevoPedidoViewModel(
    private val repo: LavaRepository,
    private val usuarioId: Int,
    repeatPedidoId: Int = -1
) : ViewModel() {
    val esRepetido: Boolean = repeatPedidoId > 0

    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _cantidades = MutableStateFlow<Map<Int, Double>>(emptyMap())
    val cantidades: StateFlow<Map<Int, Double>> = _cantidades.asStateFlow()

    private val _nota = MutableStateFlow("")
    val nota: StateFlow<String> = _nota.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val subtotal: StateFlow<Double> = combine(servicios, cantidades) { lista, cantidades ->
        lista.sumOf { servicio -> (cantidades[servicio.id] ?: 0.0) * servicio.precio }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val descuento: StateFlow<Double> = combine(servicios, cantidades) { lista, cantidades ->
        val subtotalActual = lista.sumOf { servicio -> (cantidades[servicio.id] ?: 0.0) * servicio.precio }
        val kilos = lista.filter { it.unidad == "KG" }.sumOf { cantidades[it.id] ?: 0.0 }
        if (kilos > 5.0) subtotalActual * 0.10 else 0.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val total: StateFlow<Double> = combine(subtotal, descuento) { bruto, rebaja -> bruto - rebaja }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val fechaEntrega: StateFlow<String> = combine(servicios, cantidades) { lista, cantidades ->
        val seleccionados = lista.filter { (cantidades[it.id] ?: 0.0) > 0 }
        val dias = seleccionados.maxOfOrNull { (it.horasEntrega + 23) / 24 } ?: 0
        var fecha = LocalDate.now().plusDays(dias.toLong())
        if (fecha.dayOfWeek == DayOfWeek.SUNDAY) fecha = fecha.plusDays(1)
        fecha.toString()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LocalDate.now().toString())

    val puedeConfirmar: StateFlow<Boolean> = cantidades
        .map { cantidadesActuales ->
            servicios.value.any { servicio -> (cantidadesActuales[servicio.id] ?: 0.0) > 0 }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    init {
        if (repeatPedidoId > 0) {
            viewModelScope.launch {
                val activos = servicios.first { it.isNotEmpty() }.map { it.id }.toSet()
                val pedido = repo.observarPedido(repeatPedidoId).first() ?: return@launch
                _cantidades.value = pedido.detalles
                    .filter { it.detalle.servicioId in activos }
                    .associate { it.detalle.servicioId to it.detalle.cantidad }
            }
        }
    }

    fun cantidad(servicioId: Int): Double = _cantidades.value[servicioId] ?: 0.0

    fun cambiarCantidad(servicioId: Int, delta: Double) {
        val servicio = servicios.value.firstOrNull { it.id == servicioId } ?: return
        val paso = if (servicio.unidad == "KG") 0.5 else 1.0
        val maximo = if (servicio.unidad == "KG") 10.0 else 30.0
        val actual = cantidad(servicioId)
        var nuevo = actual + delta
        if (delta > 0 && actual == 0.0) nuevo = if (servicio.unidad == "KG") 1.0 else 1.0
        nuevo = (nuevo * 2).roundToInt() / 2.0
        if (paso == 1.0) nuevo = nuevo.roundToInt().toDouble()
        if (nuevo in 0.0..0.99) nuevo = 0.0
        nuevo = nuevo.coerceIn(0.0, maximo)
        _cantidades.update { anterior ->
            if (nuevo <= 0.0) anterior - servicioId
            else anterior + (servicioId to nuevo)
        }
    }

    fun cambiarNota(valor: String) {
        _nota.value = valor
    }

    fun confirmar(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _error.value = null
            val activos = servicios.value.map { it.id }.toSet()
            val lineas = cantidades.value.filterKeys { it in activos }.filterValues { it > 0 }
                .map { (servicioId, cantidad) -> LineaPedido(servicioId, cantidad) }
            if (lineas.isEmpty()) {
                _error.value = "Selecciona al menos un servicio"
                return@launch
            }
            if (repo.contarActivos(usuarioId) >= 3) {
                _error.value = "Ya tienes 3 pedidos activos"
                return@launch
            }
            runCatching {
                repo.crearPedido(usuarioId, lineas, fechaEntrega.value, nota.value)
            }.onSuccess {
                onSuccess()
            }.onFailure {
                _error.value = it.message ?: "No se pudo crear el pedido"
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
