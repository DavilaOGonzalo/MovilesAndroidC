package com.tecsup.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.centrotareas.data.TareasRepository
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.model.RegistroCarga
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo"); val mensaje = _mensaje.asStateFlow()
    private val _progreso = MutableStateFlow(0f); val progreso = _progreso.asStateFlow()
    private val _corriendo = MutableStateFlow(false); val corriendo = _corriendo.asStateFlow()
    private var trabajo: Job? = null
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial); val primos = _primos.asStateFlow()
    private var trabajoPrimos: Job? = null
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial); val carga = _carga.asStateFlow()
    private val _simularError = MutableStateFlow(false); val simularError = _simularError.asStateFlow()
    private val _comparaciones = MutableStateFlow<List<RegistroCarga>>(emptyList()); val comparaciones = _comparaciones.asStateFlow()
    fun cambiarSimularError(valor: Boolean) { _simularError.value = valor }
    private var trabajoCarga: Job? = null

    fun tareaLarga() {
        if (_corriendo.value) return
        trabajo = viewModelScope.launch {
            _corriendo.value = true; _mensaje.value = "Trabajando..."; _progreso.value = 0f
            try {
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                withContext(Dispatchers.Default) { Log.d("HILO", "Dentro: ${Thread.currentThread().name}"); repeat(100) { i -> delay(40); _progreso.value = (i + 1) / 100f } }
                Log.d("HILO", "Después: ${Thread.currentThread().name}"); _mensaje.value = "Terminó"
            } catch (e: CancellationException) { throw e } finally { _corriendo.value = false }
        }
    }
    fun cancelar() { if (_corriendo.value) { _mensaje.value = "Cancelado"; trabajo?.cancel() } }
    fun contar(hasta: Int) {
        if (hasta < 2 || trabajoPrimos?.isActive == true) return
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando; val inicio = System.currentTimeMillis()
            try { val cantidad = repo.contarPrimos(hasta); _primos.value = PrimosUi.Listo(cantidad, System.currentTimeMillis() - inicio) }
            catch (e: CancellationException) { _primos.value = PrimosUi.Cancelado; throw e }
        }
    }
    fun cancelarPrimos() { trabajoPrimos?.cancel() }
    fun cargarUnaPorUna() = ejecutarCarga("Una por una") {
        val perfil = repo.cargarPerfil(); val pedidos = repo.cargarPedidos(_simularError.value); perfil to pedidos
    }
    fun cargarALaVez() = ejecutarCarga("A la vez") {
        coroutineScope { val perfil = async { repo.cargarPerfil() }; val pedidos = async { repo.cargarPedidos(_simularError.value) }; perfil.await() to pedidos.await() }
    }
    private fun ejecutarCarga(modo: String, bloque: suspend () -> Pair<String, List<String>>) {
        if (trabajoCarga?.isActive == true) return
        trabajoCarga = viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = cargarConReintentos(bloque)
                val tiempo = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito(modo, perfil, pedidos.size, tiempo)
                registrar(modo, tiempo, "Completada")
            } catch (e: TimeoutCancellationException) {
                val tiempo = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Error("Tardó demasiado")
                registrar(modo, tiempo, "Tardó demasiado")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val tiempo = System.currentTimeMillis() - inicio
                val mensajeError = "No se pudo cargar: ${e.message ?: "Error desconocido"}"
                _carga.value = CargaUi.Error(mensajeError)
                registrar(modo, tiempo, mensajeError)
            }
        }
    }
    private suspend fun cargarConReintentos(bloque: suspend () -> Pair<String, List<String>>): Pair<String, List<String>> {
        val maxReintentos = 3
        for (intento in 0..maxReintentos) {
            try { return withTimeout(2_000) { bloque() } }
            catch (e: TimeoutCancellationException) { if (intento == maxReintentos) throw e }
            catch (e: CancellationException) { throw e }
            catch (e: Exception) { if (intento == maxReintentos) throw e }
            delay(500L * (intento + 1))
        }
        error("No se completaron los intentos de carga")
    }
    private fun registrar(modo: String, tiempo: Long, resultado: String) {
        _comparaciones.value = _comparaciones.value + RegistroCarga(modo, tiempo, resultado)
    }
}
