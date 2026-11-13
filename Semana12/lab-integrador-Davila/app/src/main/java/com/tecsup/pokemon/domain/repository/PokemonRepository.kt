package com.tecsup.pokemon.domain.repository

import com.tecsup.pokemon.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    val pokemones: Flow<List<Pokemon>>
    val favoritos: Flow<List<Pokemon>>
    suspend fun refrescar(): Result<Int>
    suspend fun obtenerDetalle(id: String): Result<Pokemon>
    suspend fun alternarFavorito(id: String)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}
