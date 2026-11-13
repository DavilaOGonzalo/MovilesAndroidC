package com.tecsup.pokemon.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeDto(val pokemon: List<PokemonReferenceDto> = emptyList())

@Serializable
data class PokemonReferenceDto(val pokemon: PokemonReference)

@Serializable
data class PokemonReference(val name: String, val url: String)

@Serializable
data class PokemonDetailDto(
    val name: String,
    val sprites: SpriteDto = SpriteDto(),
    val base_experience: Int? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val types: List<TypeEntryDto> = emptyList(),
)

@Serializable
data class SpriteDto(val front_default: String? = null)

@Serializable
data class TypeEntryDto(val type: NamedResourceDto)

@Serializable
data class NamedResourceDto(val name: String)
