package com.tecsup.murocampus.data.remote

import com.tecsup.murocampus.model.Usuario

interface UsuarioApi {
    // Ruta del ejemplo [E]: GET users. La anotación requiere el paso 1.
    suspend fun listar(): List<Usuario>
}
