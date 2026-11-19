package com.davila.campusgps.data

import com.davila.campusgps.model.Posicion
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(
    private val fusedLocationClient: FusedLocationProviderClient,
) : UbicacionRepository {
    override suspend fun obtenerPosicion(): Result<Posicion> = try {
        val location = fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token,
        ).await() ?: return Result.failure(IllegalStateException("No se pudo obtener la ubicación."))

        Result.success(Posicion(location.latitude, location.longitude))
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}