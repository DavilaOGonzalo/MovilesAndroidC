package com.tecsup.centrotareas.data

import kotlinx.coroutines.delay

class TareasRepositoryLocal : TareasRepository {
    override suspend fun cargarPerfil(): String { delay(2_000); return "Ana Torres (ficticia)" }
    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3_000)
        if (simularError) throw java.io.IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }
    override suspend fun contarPrimos(hasta: Int): Int = 0
}
