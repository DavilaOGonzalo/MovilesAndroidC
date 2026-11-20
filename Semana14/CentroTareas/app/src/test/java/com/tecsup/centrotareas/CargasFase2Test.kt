package com.tecsup.centrotareas

import com.tecsup.centrotareas.data.TareasRepository
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.viewmodel.TareasViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

@OptIn(ExperimentalCoroutinesApi::class)
class CargasFase2Test {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test fun timeoutSeMuestraTrasTresReintentos() = runTest(mainDispatcherRule.dispatcher) {
        val repository = SlowRepository()
        val vm = TareasViewModel(repository)
        vm.cargarALaVez()
        advanceUntilIdle()
        assertEquals(CargaUi.Error("Tardó demasiado"), vm.carga.value)
        assertEquals(4, repository.perfiles.get())
        assertEquals(4, repository.pedidos.get())
    }

    @Test fun errorTransitorioSeReintentaHastaTenerExito() = runTest(mainDispatcherRule.dispatcher) {
        val repository = RetryRepository()
        val vm = TareasViewModel(repository)
        vm.cargarALaVez()
        advanceUntilIdle()
        val estado = vm.carga.value as CargaUi.Exito
        assertEquals("A la vez", estado.modo)
        assertEquals(3, repository.intentos.get())
        assertEquals(1, vm.comparaciones.value.size)
        vm.cargarUnaPorUna()
        advanceUntilIdle()
        assertEquals(2, vm.comparaciones.value.size)
        assertEquals(listOf("A la vez", "Una por una"), vm.comparaciones.value.map { it.modo })
    }
}

private class SlowRepository : TareasRepository {
    val perfiles = AtomicInteger(); val pedidos = AtomicInteger()
    override suspend fun cargarPerfil(): String { perfiles.incrementAndGet(); delay(5_000); return "Ana" }
    override suspend fun cargarPedidos(simularError: Boolean): List<String> { pedidos.incrementAndGet(); delay(5_000); return listOf("Pedido") }
    override suspend fun contarPrimos(hasta: Int): Int = 0
}

private class RetryRepository : TareasRepository {
    val intentos = AtomicInteger()
    override suspend fun cargarPerfil(): String = "Ana"
    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        if (intentos.incrementAndGet() < 3) throw IOException("Temporal")
        return listOf("Pedido 101")
    }
    override suspend fun contarPrimos(hasta: Int): Int = 0
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(val dispatcher: TestDispatcher = StandardTestDispatcher()) : TestWatcher() {
    override fun starting(description: Description) { Dispatchers.setMain(dispatcher) }
    override fun finished(description: Description) { Dispatchers.resetMain() }
}



