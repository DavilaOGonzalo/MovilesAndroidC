package com.tecsup.pokemon.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemones")
data class PokemonEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val imagenUrl: String?,
    val experienciaBase: Int? = null,
    val altura: Int? = null,
    val peso: Int? = null,
    val tiposCsv: String = "",
    val esFavorito: Boolean = false,
)
