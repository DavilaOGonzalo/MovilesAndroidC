package com.tecsup.lavacampus.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {
    private object Keys {
        val perfilActivo = stringPreferencesKey("perfilActivo")
        val usuarioId = intPreferencesKey("usuarioId")
        val modoOscuro = booleanPreferencesKey("modoOscuro")
        val notificaciones = booleanPreferencesKey("notificaciones")
        val recibirPedidos = booleanPreferencesKey("recibirPedidos")
    }

    val perfilActivo: Flow<String> = context.dataStore.data.map { it[Keys.perfilActivo] ?: "" }
    val usuarioId: Flow<Int> = context.dataStore.data.map { it[Keys.usuarioId] ?: -1 }
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[Keys.modoOscuro] ?: false }
    val notificaciones: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.notificaciones] ?: true }
    val recibirPedidos: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.recibirPedidos] ?: true }

    suspend fun guardarSesion(rol: String, usuarioId: Int) {
        context.dataStore.edit {
            it[Keys.perfilActivo] = rol
            it[Keys.usuarioId] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(valor: Boolean) {
        context.dataStore.edit { it[Keys.modoOscuro] = valor }
    }

    suspend fun guardarNotificaciones(valor: Boolean) {
        context.dataStore.edit { it[Keys.notificaciones] = valor }
    }

    suspend fun guardarRecibirPedidos(valor: Boolean) {
        context.dataStore.edit { it[Keys.recibirPedidos] = valor }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit {
            it.remove(Keys.perfilActivo)
            it.remove(Keys.usuarioId)
        }
    }
}
