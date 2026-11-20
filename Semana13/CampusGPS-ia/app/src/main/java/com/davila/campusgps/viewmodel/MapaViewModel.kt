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

    private val _lugaresParaMapa = MutableStateFlow(todosLosLugaresConDistancia)
    val lugaresParaMapa = _lugaresParaMapa.asStateFlow()

    private val _seleccionadoId = MutableStateFlow<Int?>(null)
    val seleccionadoId = _seleccionadoId.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow(CategoriaFiltro.TODOS)
    val categoriaSeleccionada = _categoriaSeleccionada.asStateFlow()

    private val _radioMetros = MutableStateFlow(RADIOS_CERCANIA_METROS[1])
    val radioMetros = _radioMetros.asStateFlow()

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

    fun seleccionarRadio(metros: Int) {
        if (metros !in RADIOS_CERCANIA_METROS || _radioMetros.value == metros) return
        _radioMetros.value = metros
        recalcularResultados()
    }

    fun seleccionar(id: Int) {
        if (lugaresBase.any { it.id == id }) _seleccionadoId.value = id
    }

    fun irAlMasCercano() {
        val cercano = _lugarMasCercano.value ?: return
        val categoria = CategoriaFiltro.entries.firstOrNull { it.categoria == cercano.lugar.categoria }
        if (categoria != null) _categoriaSeleccionada.value = categoria
        val radioQueLoIncluye = RADIOS_CERCANIA_METROS.firstOrNull {
            cercano.metros != null && cercano.metros <= it
        } ?: RADIOS_CERCANIA_METROS.last()
        _radioMetros.value = maxOf(_radioMetros.value, radioQueLoIncluye)
        recalcularResultados()
        seleccionar(cercano.lugar.id)
    }

    private fun actualizarDistancias(posicion: Posicion) {
        posicionActual = posicion
        recalcularResultados()
    }

    private var posicionActual: Posicion? = null

    private fun recalcularResultados() {
        val radio = _radioMetros.value
        val ordenados = lugaresBase
            .map { lugar ->
                val metros = posicionActual?.let { metrosDesde(it, lugar) }
                LugarConDistancia(lugar, metros, metros?.let { it <= radio })
            }
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
        _lugaresParaMapa.value = filtrados
        _lugaresConDistancia.value = if (posicionActual == null) filtrados else {
            filtrados.filter { it.dentroDelRadio == true }
        }
    }

    companion object {
        val RADIOS_CERCANIA_METROS = listOf(100, 200, 500)
    }
}
