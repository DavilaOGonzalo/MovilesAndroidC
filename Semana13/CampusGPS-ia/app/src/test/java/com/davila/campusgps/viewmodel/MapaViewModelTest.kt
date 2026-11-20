package com.davila.campusgps.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.davila.campusgps.data.LugaresRepositoryLocal
import com.davila.campusgps.data.UbicacionRepository
import com.davila.campusgps.data.UltimoLugarVisitadoRepository
import com.davila.campusgps.model.CategoriaFiltro
import com.davila.campusgps.model.Posicion
import com.davila.campusgps.model.TipoErrorUbicacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapaViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun filtroDeCategoriaYRadioActualizaMapaYLista() = runTest(mainDispatcherRule.dispatcher) {
        val almacenamiento = AlmacenamientoLugarFake()
        val (viewModel, store) = crearViewModel(almacenamiento)
        try {
            viewModel.seleccionarCategoria(CategoriaFiltro.ESTUDIO)
            viewModel.seleccionarRadio(100)
            viewModel.buscar()
            advanceUntilIdle()

            assertEquals(setOf(1, 3), viewModel.lugaresParaMapa.value.map { it.lugar.id }.toSet())
            assertEquals(setOf(1, 3), viewModel.lugaresConDistancia.value.map { it.lugar.id }.toSet())

            viewModel.seleccionarCategoria(CategoriaFiltro.TODOS)
            val estacionamiento = viewModel.lugaresParaMapa.value.first { it.lugar.id == 5 }
            assertFalse(estacionamiento.dentroDelRadio == true)

            viewModel.seleccionarCategoria(CategoriaFiltro.COMIDA)
            assertEquals(listOf(2), viewModel.lugaresParaMapa.value.map { it.lugar.id })
            assertEquals(listOf(2), viewModel.lugaresConDistancia.value.map { it.lugar.id })

            viewModel.seleccionarCategoria(CategoriaFiltro.TODOS)
            viewModel.seleccionarRadio(500)
            assertEquals(5, viewModel.lugaresConDistancia.value.size)
            assertTrue(viewModel.lugaresConDistancia.value.all { it.dentroDelRadio == true })
        } finally {
            store.clear()
        }
    }

    @Test
    fun restauraUltimoLugarYGuardaNuevaSeleccion() = runTest(mainDispatcherRule.dispatcher) {
        val almacenamiento = AlmacenamientoLugarFake(ultimoGuardado = 4)
        val (viewModel, store) = crearViewModel(almacenamiento)
        try {
            advanceUntilIdle()
            assertEquals(4, viewModel.seleccionadoId.value)
            assertEquals("Auditorio Principal", viewModel.lugarSeleccionado.value?.nombre)

            viewModel.seleccionar(2)
            advanceUntilIdle()
            assertEquals(2, almacenamiento.ultimoGuardado.value)
        } finally {
            store.clear()
        }
    }

    @Test
    fun irAlMasCercanoMantieneElMarcadorNavegableAunqueLosFiltrosLoOculten() =
        runTest(mainDispatcherRule.dispatcher) {
            val almacenamiento = AlmacenamientoLugarFake()
            val (viewModel, store) = crearViewModel(almacenamiento)
            try {
                viewModel.seleccionarCategoria(CategoriaFiltro.SERVICIOS)
                viewModel.seleccionarRadio(100)
                viewModel.buscar()
                advanceUntilIdle()

                viewModel.irAlMasCercano()
                advanceUntilIdle()

                assertEquals(4, viewModel.seleccionadoId.value)
                assertEquals(CategoriaFiltro.EVENTOS, viewModel.categoriaSeleccionada.value)
                assertTrue(viewModel.lugaresConDistancia.value.any { it.lugar.id == 4 })
                assertEquals(4, almacenamiento.ultimoGuardado.value)
            } finally {
                store.clear()
            }
        }

    private fun crearViewModel(
        almacenamiento: AlmacenamientoLugarFake,
    ): Pair<MapaViewModel, ViewModelStore> {
        val store = ViewModelStore()
        val factory = CampusViewModelFactory(
            ubicacionRepository = UbicacionFake(),
            lugaresRepository = LugaresRepositoryLocal(),
            ultimoLugarVisitadoRepository = almacenamiento,
        )
        val viewModel = ViewModelProvider(store, factory)[MapaViewModel::class.java]
        return viewModel to store
    }
}

private class UbicacionFake : UbicacionRepository {
    override fun playServicesDisponible() = true

    override suspend fun obtenerPosicion(): Result<Posicion> = Result.success(
        Posicion(-12.044824, -76.952685),
    )
}

private class AlmacenamientoLugarFake(ultimoGuardado: Int? = null) : UltimoLugarVisitadoRepository {
    private val mutableUltimoGuardado = MutableStateFlow(ultimoGuardado)
    val ultimoGuardado: StateFlow<Int?> = mutableUltimoGuardado.asStateFlow()
    override val ultimoLugarId = mutableUltimoGuardado.asStateFlow()

    override suspend fun guardarUltimoLugar(id: Int) {
        mutableUltimoGuardado.value = id
    }
}
