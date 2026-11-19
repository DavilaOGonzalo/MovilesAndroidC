package com.tecsup.pokemon.data.repository

import com.tecsup.pokemon.data.local.PokemonDao
import com.tecsup.pokemon.data.mapper.aEntity
import com.tecsup.pokemon.data.mapper.aModelo
import com.tecsup.pokemon.data.remote.ApiService
import com.tecsup.pokemon.domain.model.Pokemon
import com.tecsup.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

class PokemonRepositoryImpl(
    private val api: ApiService,
    private val dao: PokemonDao,
) : PokemonRepository {
    override val pokemones: Flow<List<Pokemon>> = dao.todos().map { rows -> rows.map { it.aModelo() } }
    override val favoritos: Flow<List<Pokemon>> = dao.favoritos().map { rows -> rows.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = try {
        val favoritos = dao.idsFavoritos().toSet()
        val entities = api.listarTipoFuego().pokemon.map { reference ->
            val pokemon = reference.pokemon
            val spriteId = pokemon.url.trimEnd('/').substringAfterLast('/').toIntOrNull()
            com.tecsup.pokemon.data.local.PokemonEntity(
                id = pokemon.name,
                nombre = pokemon.name,
                imagenUrl = spriteId?.let {
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$it.png"
                },
                esFavorito = pokemon.name in favoritos,
            )
        }
        dao.guardarTodos(entities)
        Result.success(entities.size)
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: IOException) {
        Result.failure(exception)
    } catch (exception: HttpException) {
        Result.failure(exception)
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    override suspend fun obtenerDetalle(id: String): Result<Pokemon> {
        val local = dao.porId(id)
        return try {
            val detalle = api.obtenerPokemon(id).aModelo(esFavorito = local?.esFavorito == true)
            dao.guardar(detalle.aEntity())
            Result.success(detalle)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: IOException) {
            if (local != null) Result.success(local.aModelo()) else Result.failure(exception)
        } catch (exception: HttpException) {
            if (local != null) Result.success(local.aModelo()) else Result.failure(exception)
        } catch (exception: Exception) {
            if (local != null) Result.success(local.aModelo()) else Result.failure(exception)
        }
    }

    override suspend fun alternarFavorito(id: String) = dao.alternarFavorito(id)
    override suspend fun cantidadGuardada(): Int = dao.contar()
    override suspend fun cantidadFavoritos(): Int = dao.contarFavoritos()
    override suspend fun limpiarCache() = dao.borrarTodo()
}
