package com.tecsup.pokemon.data.remote

import com.tecsup.pokemon.data.remote.dto.PokemonDetailDto
import com.tecsup.pokemon.data.remote.dto.PokemonTypeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("type/fire")
    suspend fun listarTipoFuego(): PokemonTypeDto

    @GET("pokemon/{nombre}")
    suspend fun obtenerPokemon(@Path("nombre") nombre: String): PokemonDetailDto
}
