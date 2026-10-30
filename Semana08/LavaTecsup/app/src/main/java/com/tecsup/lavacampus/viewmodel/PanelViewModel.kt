package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.DatoGrafico
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class PanelViewModel(private val repo: LavaRepository) : ViewModel() {
    private val hoy = LocalDate.now().toString()
    private val inicioSemana = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .toString()

    val pedidosHoy: StateFlow<Int> = repo.observarPedidosDeFecha(hoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val enProceso: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val porRecoger: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.LISTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val atrasados: StateFlow<Int> = repo.observarAtrasados(hoy)
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)
    val ingresosSemana: StateFlow<Double> = repo.observarIngresosDesde(inicioSemana)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO),
        repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO),
        repo.observarCantidadPorEstado(EstadoPedido.LISTO),
        repo.observarCantidadPorEstado(EstadoPedido.ENTREGADO)
    ) { recibido, proceso, listo, entregado ->
        listOf(
            dato(EstadoPedido.RECIBIDO, recibido),
            dato(EstadoPedido.EN_PROCESO, proceso),
            dato(EstadoPedido.LISTO, listo),
            dato(EstadoPedido.ENTREGADO, entregado)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarIngresosPorServicio(inicioSemana)
        .map { ingresos ->
            ingresos.mapIndexed { index, ingreso ->
                DatoGrafico(
                    etiqueta = ingreso.nombre,
                    valor = ingreso.total.toFloat(),
                    colorHex = listOf(0xFF6D28D9, 0xFF8B5CF6, 0xFFA78BFA, 0xFFC4B5FD)[index % 4]
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private fun dato(estado: EstadoPedido, cantidad: Int) = DatoGrafico(
        etiqueta = estado.etiqueta,
        valor = cantidad.toFloat(),
        colorHex = estado.colorHex
    )
}
