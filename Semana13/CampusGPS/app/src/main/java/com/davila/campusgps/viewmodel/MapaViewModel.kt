package com.davila.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davila.campusgps.data.LugaresRepository
import com.davila.campusgps.data.UbicacionRepository
import com.davila.campusgps.model.LugarConDistancia
import com.davila.campusgps.model.Posicion
import com.davila.campusgps.model.UbicacionUi
import com.davila.campusgps.util.metrosDesde
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepository: UbicacionRepository,
    lugaresRepository: LugaresRepository,
) : ViewModel() {
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado = _estado.asStateFlow()

    private val _lugaresConDistancia = MutableStateFlow(
        lugaresRepository.obtenerLugares().map { lugar -> LugarConDistancia(lugar, null) },
    )
    val lugaresConDistancia = _lugaresConDistancia.asStateFlow()

    private val _lugarMasCercano = MutableStateFlow<LugarConDistancia?>(null)
    val lugarMasCercano = _lugarMasCercano.asStateFlow()

    private val _lugares = MutableStateFlow(_lugaresConDistancia.value.map { it.lugar })
    val lugares = _lugares.asStateFlow()

    private val _seleccionadoId = MutableStateFlow<Int?>(null)
    val seleccionadoId = _seleccionadoId.asStateFlow()

    fun buscar() {
        if (_estado.value is UbicacionUi.Buscando) return

        viewModelScope.launch {
            _estado.value = UbicacionUi.Buscando
            ubicacionRepository.obtenerPosicion()
                .onSuccess { posicion ->
                    actualizarDistancias(posicion)
                    _estado.value = UbicacionUi.Disponible(posicion)
                }
                .onFailure { error ->
                    _estado.value = UbicacionUi.Error(
                        error.message ?: "No se pudo obtener la ubicación.",
                    )
                }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.PermisoDenegado
    }

    fun seleccionar(id: Int) {
        if (_lugaresConDistancia.value.any { it.lugar.id == id }) {
            _seleccionadoId.value = id
        }
    }

    private fun actualizarDistancias(posicion: Posicion) {
        val ordenados = _lugaresConDistancia.value
            .map { item -> item.copy(metros = metrosDesde(posicion, item.lugar)) }
            .sortedBy { item -> item.metros ?: Float.POSITIVE_INFINITY }

        _lugaresConDistancia.value = ordenados
        _lugares.value = ordenados.map { it.lugar }
        _lugarMasCercano.value = ordenados.firstOrNull { it.metros != null }
    }
}
