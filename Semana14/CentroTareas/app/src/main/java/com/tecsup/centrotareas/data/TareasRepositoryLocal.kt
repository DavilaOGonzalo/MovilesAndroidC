package com.tecsup.centrotareas.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.coroutineContext

class TareasRepositoryLocal : TareasRepository {
    override suspend fun cargarPerfil(): String { delay(2_000); return "Ana Torres (ficticia)" }
    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3_000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }
    override suspend fun contarPrimos(hasta: Int): Int = withContext(Dispatchers.Default) {
        var cantidad = 0
        for (n in 2..hasta) { coroutineContext.ensureActive(); if (esPrimo(n)) cantidad++ }
        cantidad
    }
    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false
        var d = 2
        while (d.toLong() * d <= n) { if (n % d == 0) return false; d++ }
        return true
    }
}
