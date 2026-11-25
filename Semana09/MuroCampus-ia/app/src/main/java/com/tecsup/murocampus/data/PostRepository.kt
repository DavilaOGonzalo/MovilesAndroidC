package com.tecsup.murocampus.data

import com.tecsup.murocampus.data.remote.PostApi
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun eliminar(id: Int): Result<Unit>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
}

class ErrorDeDatos internal constructor(mensaje: String, causa: Throwable) :
    Exception(mensaje, causa)

class PostRepositoryRemote(private val api: PostApi) : PostRepository {
    override suspend fun listar(): Result<List<Post>> = llamar { api.listar() }

    override suspend fun detalle(id: Int): Result<Post> = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int): Result<List<Comentario>> =
        llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int): Result<List<Post>> =
        llamar { api.porUsuario(userId) }

    override suspend fun eliminar(id: Int): Result<Unit> = llamar {
        val respuesta = api.eliminar(id)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
    }

    override suspend fun crear(nuevo: NuevoPost): Result<Post> = llamar { api.crear(nuevo) }

    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val mensaje = when (e) {
                is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
                is HttpException -> "Error del servidor (${e.code()})"
                is SerializationException -> "Respuesta inesperada"
                else -> "Error desconocido"
            }
            Result.failure(ErrorDeDatos(mensaje, e))
        }
}
