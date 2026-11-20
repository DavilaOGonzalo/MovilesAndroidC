package com.tecsup.centrotareas

import com.tecsup.centrotareas.data.TareasRepositoryLocal
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TareasRepositoryLocalTest {
    private val repository = TareasRepositoryLocal()

    @Test fun cuentaPrimosHasta100() = runTest { assertEquals(25, repository.contarPrimos(100)) }
    @Test fun cuentaPrimosHasta1000() = runTest { assertEquals(168, repository.contarPrimos(1_000)) }
    @Test fun cuentaPrimosHasta10000() = runTest { assertEquals(1_229, repository.contarPrimos(10_000)) }
}
