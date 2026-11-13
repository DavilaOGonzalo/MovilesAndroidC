package com.tecsup.pokemon.domain.model

data class Pokemon(
    val id: String,
    val nombre: String,
    val imagenUrl: String?,
    val experienciaBase: Int? = null,
    val altura: Int? = null,
    val peso: Int? = null,
    val tipos: List<String> = emptyList(),
    val esFavorito: Boolean = false,
)
