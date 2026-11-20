package com.davila.campusgps.data

import android.content.Context
import android.location.LocationManager
import android.os.Build
import com.davila.campusgps.model.ErrorUbicacion
import com.davila.campusgps.model.Posicion
import com.davila.campusgps.model.TipoErrorUbicacion
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(
    context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
) : UbicacionRepository {
    private val appContext = context.applicationContext

    override fun playServicesDisponible(): Boolean = GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(appContext) == ConnectionResult.SUCCESS

    override suspend fun obtenerPosicion(): Result<Posicion> {
        return try {
            if (!playServicesDisponible()) {
                return Result.failure(ErrorUbicacion(
                    TipoErrorUbicacion.SIN_PLAY_SERVICES,
                    "Este dispositivo no tiene Google Play Services.",
                ))
            }

            val locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsActivo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                locationManager.isLocationEnabled
            } else {
                @Suppress("DEPRECATION")
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            }
            if (!gpsActivo) {
                return Result.failure(
                    ErrorUbicacion(TipoErrorUbicacion.GPS_APAGADO, "Activa el GPS e intenta de nuevo."),
                )
            }

            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token,
            ).await() ?: return Result.failure(
                ErrorUbicacion(TipoErrorUbicacion.GPS_APAGADO, "Activa el GPS e intenta de nuevo."),
            )

            Result.success(Posicion(location.latitude, location.longitude))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: SecurityException) {
            Result.failure(ErrorUbicacion(TipoErrorUbicacion.SIN_PERMISO, "Falta el permiso de ubicación."))
        } catch (exception: Exception) {
            Result.failure(ErrorUbicacion(TipoErrorUbicacion.OTRO, exception.message ?: "No se pudo obtener la ubicación."))
        }
    }
}
