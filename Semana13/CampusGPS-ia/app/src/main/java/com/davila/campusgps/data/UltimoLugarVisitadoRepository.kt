package com.davila.campusgps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.campusGpsDataStore: DataStore<Preferences> by preferencesDataStore(name = "campusgps")

interface UltimoLugarVisitadoRepository {
    val ultimoLugarId: Flow<Int?>
    suspend fun guardarUltimoLugar(id: Int)
}

class UltimoLugarVisitadoRepositoryDataStore(
    private val dataStore: DataStore<Preferences>,
) : UltimoLugarVisitadoRepository {
    override val ultimoLugarId: Flow<Int?> = dataStore.data.map { preferencias ->
        preferencias[ULTIMO_LUGAR_ID]
    }

    override suspend fun guardarUltimoLugar(id: Int) {
        dataStore.edit { preferencias ->
            preferencias[ULTIMO_LUGAR_ID] = id
        }
    }

    private companion object {
        val ULTIMO_LUGAR_ID = intPreferencesKey("ultimo_lugar_visitado_id")
    }
}
