package com.davila.campusgps.data

import com.davila.campusgps.model.Posicion

interface UbicacionRepository {
    fun playServicesDisponible(): Boolean
    suspend fun obtenerPosicion(): Result<Posicion>
}
