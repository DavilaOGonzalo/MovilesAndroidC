package com.tecsup.murocampus.data.remote

import com.tecsup.murocampus.model.Usuario
import retrofit2.http.GET

interface UsuarioApi {
    @GET("users")
    suspend fun listar(): List<Usuario>
}
