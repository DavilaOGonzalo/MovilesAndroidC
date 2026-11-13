package com.tecsup.pokemon.data.mapper

import com.tecsup.pokemon.data.local.PokemonEntity
import com.tecsup.pokemon.data.remote.dto.PokemonDetailDto
import com.tecsup.pokemon.domain.model.Pokemon

fun PokemonEntity.aModelo(): Pokemon = Pokemon(
    id = id,
    nombre = nombre,
    imagenUrl = imagenUrl,
    experienciaBase = experienciaBase,
    altura = altura,
    peso = peso,
    tipos = tiposCsv.split(',').filter(String::isNotBlank),
    esFavorito = esFavorito,
)

fun Pokemon.aEntity(): PokemonEntity = PokemonEntity(
    id = id,
    nombre = nombre,
    imagenUrl = imagenUrl,
    experienciaBase = experienciaBase,
    altura = altura,
    peso = peso,
    tiposCsv = tipos.joinToString(","),
    esFavorito = esFavorito,
)

fun PokemonDetailDto.aModelo(esFavorito: Boolean = false): Pokemon = Pokemon(
    id = name,
    nombre = name,
    imagenUrl = sprites.front_default,
    experienciaBase = base_experience,
    altura = height,
    peso = weight,
    tipos = types.map { it.type.name },
    esFavorito = esFavorito,
)
