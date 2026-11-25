package com.tecsup.murocampus.data

import com.tecsup.murocampus.data.remote.UsuarioApi
import com.tecsup.murocampus.model.Usuario
import kotlinx.coroutines.CancellationException

interface UsuarioRepository {
    suspend fun listar(): Result<List<Usuario>>
}

class UsuarioRepositoryRemote(private val api: UsuarioApi) : UsuarioRepository {
    override suspend fun listar(): Result<List<Usuario>> =
        try {
            Result.success(api.listar())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}
