package com.tecsup.pokemon.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.pokemon.domain.model.Rol
import com.tecsup.pokemon.domain.model.Sesion
import com.tecsup.pokemon.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : AuthRepository {
    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val result = auth.signInWithEmailAndPassword(correo, clave).await()
        val user = requireNotNull(result.user) { "Firebase no devolvió un usuario autenticado." }
        Result.success(leerSesion(user.uid, user.email.orEmpty()))
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        auth.signOut()
        Result.failure(exception)
    }

    override suspend fun sesionActual(): Sesion? {
        val user = auth.currentUser ?: return null
        return try {
            leerSesion(user.uid, user.email.orEmpty())
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            auth.signOut()
            null
        }
    }

    override fun cerrarSesion() = auth.signOut()

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val snapshot = firestore.collection("usuarios").document(uid).get().await()
        val rol = when (snapshot.getString("rol")?.lowercase()) {
            "admin" -> Rol.ADMIN
            "user" -> Rol.USUARIO
            else -> error("El perfil de Firestore debe tener rol 'admin' o 'user'.")
        }
        return Sesion(
            uid = uid,
            correo = correo,
            rol = rol,
            nombre = snapshot.getString("nombre")?.takeIf(String::isNotBlank) ?: correo,
        )
    }
}
