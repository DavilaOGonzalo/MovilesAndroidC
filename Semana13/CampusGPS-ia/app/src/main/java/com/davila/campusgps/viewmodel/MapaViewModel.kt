package com.davila.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davila.campusgps.data.LugaresRepository
import com.davila.campusgps.data.UbicacionRepository
import com.davila.campusgps.data.UltimoLugarVisitadoRepository
import com.davila.campusgps.model.CategoriaFiltro
import com.davila.campusgps.model.ErrorUbicacion
import com.davila.campusgps.model.Lugar
import com.davila.campusgps.model.LugarConDistancia
import com.davila.campusgps.model.Posicion
import com.davila.campusgps.model.TipoErrorUbicacion
import com.davila.campusgps.model.UbicacionUi
import com.davila.campusgps.util.metrosDesde
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepository: UbicacionRepository,
    lugaresRepository: LugaresRepository,
    private val ultimoLugarVisitadoRepository: UltimoLugarVisitadoRepository,
) : ViewModel() {
    private val lugaresBase = lugaresRepository.obtenerLugares()
    private var posicionActual: Posicion? = null

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado = _estado.asStateFlow()

    private val _lugaresParaMapa = MutableStateFlow<List<LugarConDistancia>>(emptyList())
    val lugaresParaMapa = _lugaresParaMapa.asStateFlow()

    private val _lugaresConDistancia = MutableStateFlow<List<LugarConDistancia>>(emptyList())
    val lugaresConDistancia = _lugaresConDistancia.asStateFlow()

    private val _lugarMasCercano = MutableStateFlow<LugarConDistancia?>(null)
    val lugarMasCercano = _lugarMasCercano.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow(CategoriaFiltro.TODOS)
    val categoriaSeleccionada = _categoriaSeleccionada.asStateFlow()

    private val _radioMetros = MutableStateFlow(RADIOS_CERCANIA_METROS[1])
    val radioMetros = _radioMetros.asStateFlow()

    private val _seleccionadoId = MutableStateFlow<Int?>(null)
    val seleccionadoId = _seleccionadoId.asStateFlow()

    private val _lugarSeleccionado = MutableStateFlow<Lugar?>(null)
    val lugarSeleccionado = _lugarSeleccionado.asStateFlow()

    private val _errorPersistencia = MutableStateFlow(false)
    val errorPersistencia = _errorPersistencia.asStateFlow()

    init {
        recalcularResultados()
        viewModelScope.launch {
            ultimoLugarVisitadoRepository.ultimoLugarId
                .catch { error ->
                    if (error is IOException) emit(null) else throw error
                }
                .collect { idGuardado ->
                    if (_seleccionadoId.value == null && idGuardado != null) {
                        lugaresBase.firstOrNull { it.id == idGuardado }?.let(::actualizarSeleccion)
                    }
                }
        }
    }

    fun buscar() {
        if (_estado.value is UbicacionUi.Buscando) return

        viewModelScope.launch {
            _estado.value = UbicacionUi.Buscando
            if (!ubicacionRepository.playServicesDisponible()) {
                _estado.value = UbicacionUi.Error(
                    TipoErrorUbicacion.SIN_PLAY_SERVICES,
                    "Este dispositivo no tiene Google Play Services.",
                )
                return@launch
            }
            ubicacionRepository.obtenerPosicion()
                .onSuccess { posicion ->
                    posicionActual = posicion
                    recalcularResultados()
                    _estado.value = UbicacionUi.Disponible(posicion)
                }
                .onFailure { error ->
                    _estado.value = if (error is ErrorUbicacion && error.tipo == TipoErrorUbicacion.SIN_PERMISO) {
                        UbicacionUi.PermisoDenegado
                    } else if (error is ErrorUbicacion) {
                        UbicacionUi.Error(error.tipo, error.message ?: "No se pudo obtener la ubicación.")
                    } else {
                        UbicacionUi.Error(
                            TipoErrorUbicacion.OTRO,
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
        recalcularResultados()
    }

    fun seleccionarRadio(metros: Int) {
        if (metros !in RADIOS_CERCANIA_METROS || _radioMetros.value == metros) return
        _radioMetros.value = metros
        recalcularResultados()
    }

    fun seleccionar(id: Int) {
        val lugar = lugaresBase.firstOrNull { it.id == id } ?: return
        actualizarSeleccion(lugar)
        viewModelScope.launch {
            try {
                ultimoLugarVisitadoRepository.guardarUltimoLugar(id)
                _errorPersistencia.value = false
            } catch (_: IOException) {
                _errorPersistencia.value = true
            }
        }
    }

    fun irAlMasCercano() {
        val cercano = _lugarMasCercano.value ?: return
        val categoriaCercana = CategoriaFiltro.entries.firstOrNull {
            it.categoria == cercano.lugar.categoria
        }
        if (categoriaCercana != null && _categoriaSeleccionada.value != categoriaCercana) {
            _categoriaSeleccionada.value = categoriaCercana
            recalcularResultados()
        }
        val radioQueLoIncluye = RADIOS_CERCANIA_METROS.firstOrNull { radio ->
            cercano.metros != null && cercano.metros <= radio
        } ?: RADIOS_CERCANIA_METROS.last()
        if (_radioMetros.value < radioQueLoIncluye) seleccionarRadio(radioQueLoIncluye)
        seleccionar(cercano.lugar.id)
    }

    private fun actualizarSeleccion(lugar: Lugar) {
        _seleccionadoId.value = lugar.id
        _lugarSeleccionado.value = lugar
    }

    private fun recalcularResultados() {
        val radio = _radioMetros.value
        val todosConDistancia = lugaresBase
            .map { lugar ->
                val metros = posicionActual?.let { posicion -> metrosDesde(posicion, lugar) }
                LugarConDistancia(
                    lugar = lugar,
                    metros = metros,
                    dentroDelRadio = metros?.let { it <= radio },
                )
            }
            .sortedBy { it.metros ?: Float.POSITIVE_INFINITY }

        _lugarMasCercano.value = todosConDistancia.firstOrNull { it.metros != null }

        val categoria = _categoriaSeleccionada.value.categoria
        val lugaresDeCategoria = if (categoria == null) {
            todosConDistancia
        } else {
            todosConDistancia.filter { it.lugar.categoria == categoria }
        }
        _lugaresParaMapa.value = lugaresDeCategoria
        _lugaresConDistancia.value = if (posicionActual == null) {
            lugaresDeCategoria
        } else {
            lugaresDeCategoria.filter { it.dentroDelRadio == true }
        }
    }

    companion object {
        val RADIOS_CERCANIA_METROS = listOf(100, 200, 500)
    }
}
