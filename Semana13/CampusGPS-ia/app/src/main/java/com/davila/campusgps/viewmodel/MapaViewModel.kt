package com.davila.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davila.campusgps.data.LugaresRepository
import com.davila.campusgps.data.UbicacionRepository
import com.davila.campusgps.model.LugarConDistancia
import com.davila.campusgps.model.CategoriaFiltro
import com.davila.campusgps.model.ErrorUbicacion
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
    private val lugaresBase = lugaresRepository.obtenerLugares()
    private var todosLosLugaresConDistancia = lugaresBase.map { LugarConDistancia(it, null) }

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado = _estado.asStateFlow()

    private val _lugaresConDistancia = MutableStateFlow(todosLosLugaresConDistancia)
    val lugaresConDistancia = _lugaresConDistancia.asStateFlow()

    private val _lugarMasCercano = MutableStateFlow<LugarConDistancia?>(null)
    val lugarMasCercano = _lugarMasCercano.asStateFlow()

    private val _lugares = MutableStateFlow(_lugaresConDistancia.value.map { it.lugar })
    val lugares = _lugares.asStateFlow()

    private val _seleccionadoId = MutableStateFlow<Int?>(null)
    val seleccionadoId = _seleccionadoId.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow(CategoriaFiltro.TODOS)
    val categoriaSeleccionada = _categoriaSeleccionada.asStateFlow()

    fun buscar() {
        if (_estado.value is UbicacionUi.Buscando) return

        viewModelScope.launch {
            _estado.value = UbicacionUi.Buscando
            if (!ubicacionRepository.playServicesDisponible()) {
                _estado.value = UbicacionUi.Error(
                    com.davila.campusgps.model.TipoErrorUbicacion.SIN_PLAY_SERVICES,
                    "Este dispositivo no tiene Google Play Services.",
                )
                return@launch
            }
            ubicacionRepository.obtenerPosicion()
                .onSuccess { posicion ->
                    actualizarDistancias(posicion)
                    _estado.value = UbicacionUi.Disponible(posicion)
                }
                .onFailure { error ->
                    _estado.value = if (error is ErrorUbicacion && error.tipo == com.davila.campusgps.model.TipoErrorUbicacion.SIN_PERMISO) {
                        UbicacionUi.PermisoDenegado
                    } else if (error is ErrorUbicacion) {
                        UbicacionUi.Error(error.tipo, error.message ?: "No se pudo obtener la ubicación.")
                    } else {
                        UbicacionUi.Error(
                            com.davila.campusgps.model.TipoErrorUbicacion.OTRO,
                            error.message ?: "No se pudo obtener la ubicación.",
                        )
                    }
                }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.PermisoDenegado
    }

    fun seleccionarCategoria(categoria: CategoriaFiltro) {
        if (_categoriaSeleccionada.value == categoria) return
        _categoriaSeleccionada.value = categoria
        aplicarFiltroCategoria()
    }

    fun seleccionar(id: Int) {
        if (_lugaresConDistancia.value.any { it.lugar.id == id }) {
            _seleccionadoId.value = id
        }
    }

    private fun actualizarDistancias(posicion: Posicion) {
        val ordenados = todosLosLugaresConDistancia
            .map { item -> item.copy(metros = metrosDesde(posicion, item.lugar)) }
            .sortedBy { item -> item.metros ?: Float.POSITIVE_INFINITY }

        todosLosLugaresConDistancia = ordenados
        _lugarMasCercano.value = ordenados.firstOrNull { it.metros != null }
        aplicarFiltroCategoria()
    }

    private fun aplicarFiltroCategoria() {
        val categoria = _categoriaSeleccionada.value.categoria
        val filtrados = if (categoria == null) todosLosLugaresConDistancia else {
            todosLosLugaresConDistancia.filter { it.lugar.categoria == categoria }
        }
        _lugaresConDistancia.value = filtrados
        _lugares.value = filtrados.map { it.lugar }
    }
}
