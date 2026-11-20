package com.davila.campusgps.data

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import java.io.File
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class UltimoLugarVisitadoRepositoryDataStoreTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun guardaYRecuperaElIdDelUltimoLugar() = runTest {
        val archivo = File(temporaryFolder.root, "campusgps.preferences_pb")
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { archivo },
        )
        val repository = UltimoLugarVisitadoRepositoryDataStore(dataStore)

        repository.guardarUltimoLugar(3)

        assertEquals(3, repository.ultimoLugarId.first())
    }
}
