package com.tecsup.pokemon.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemones ORDER BY nombre")
    fun todos(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemones WHERE esFavorito = 1 ORDER BY nombre")
    fun favoritos(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemones WHERE id = :id LIMIT 1")
    suspend fun porId(id: String): PokemonEntity?

    @Query("SELECT id FROM pokemones WHERE esFavorito = 1")
    suspend fun idsFavoritos(): List<String>

    @Query("SELECT COUNT(*) FROM pokemones")
    suspend fun contar(): Int

    @Query("SELECT COUNT(*) FROM pokemones WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int

    @Upsert
    suspend fun guardarTodos(pokemones: List<PokemonEntity>)

    @Upsert
    suspend fun guardar(pokemon: PokemonEntity)

    @Query("UPDATE pokemones SET esFavorito = NOT esFavorito WHERE id = :id")
    suspend fun alternarFavorito(id: String)

    @Query("DELETE FROM pokemones")
    suspend fun borrarTodo()
}
