package com.tecsup.pokemon.ui.lista

import com.tecsup.pokemon.domain.model.Pokemon
import com.tecsup.pokemon.domain.repository.PokemonRepository
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {
    @Test
    fun `sincronizar carga y publica los datos del repositorio`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val pokemon = Pokemon("charmander", "charmander", null)
            val repository = FakePokemonRepository().apply {
                onRefresh = {
                    rows.value = listOf(pokemon)
                    Result.success(1)
                }
            }
            val notifier = FakeNotifier()

            val viewModel = ListaViewModel(repository, notifier)
            advanceUntilIdle()

            assertEquals(UiState.Exito(listOf(pokemon)), viewModel.estado.value)
            assertEquals("Se guardaron 1 elementos", notifier.lastText)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `fallo sin cache muestra estado de error`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val repository = FakePokemonRepository().apply {
                onRefresh = { Result.failure(IllegalStateException("sin conexión")) }
            }
            val viewModel = ListaViewModel(repository, FakeNotifier())
            advanceUntilIdle()

            assertTrue(viewModel.estado.value is UiState.Error)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `alternar favorito delega el cambio al repositorio`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val pokemon = Pokemon("charmander", "charmander", null)
            val repository = FakePokemonRepository().apply {
                onRefresh = {
                    rows.value = listOf(pokemon)
                    Result.success(1)
                }
            }
            val viewModel = ListaViewModel(repository, FakeNotifier())
            advanceUntilIdle()

            viewModel.alternarFavorito("charmander")
            advanceUntilIdle()

            assertEquals(listOf("charmander"), repository.favorites.value.map(Pokemon::id))
        } finally {
            Dispatchers.resetMain()
        }
    }
}

private class FakeNotifier : Notificador {
    var lastText: String? = null
    override fun mostrar(titulo: String, texto: String) { lastText = texto }
}

private class FakePokemonRepository : PokemonRepository {
    val rows = MutableStateFlow<List<Pokemon>>(emptyList())
    val favorites = MutableStateFlow<List<Pokemon>>(emptyList())
    var onRefresh: suspend () -> Result<Int> = { Result.success(0) }

    override val pokemones: Flow<List<Pokemon>> = rows
    override val favoritos: Flow<List<Pokemon>> = favorites
    override suspend fun refrescar(): Result<Int> = onRefresh()
    override suspend fun obtenerDetalle(id: String): Result<Pokemon> {
        val pokemon = rows.value.firstOrNull { it.id == id }
        return if (pokemon != null) Result.success(pokemon) else Result.failure(NoSuchElementException(id))
    }

    override suspend fun alternarFavorito(id: String) {
        val pokemon = rows.value.firstOrNull { it.id == id } ?: return
        val updated = pokemon.copy(esFavorito = !pokemon.esFavorito)
        rows.value = rows.value.map { if (it.id == id) updated else it }
        favorites.value = rows.value.filter(Pokemon::esFavorito)
    }

    override suspend fun cantidadGuardada(): Int = rows.value.size
    override suspend fun cantidadFavoritos(): Int = favorites.value.size
    override suspend fun limpiarCache() { rows.value = emptyList(); favorites.value = emptyList() }
}
