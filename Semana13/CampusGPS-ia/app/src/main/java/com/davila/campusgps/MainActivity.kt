package com.davila.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.davila.campusgps.data.LugaresRepositoryLocal
import com.davila.campusgps.data.UltimoLugarVisitadoRepositoryDataStore
import com.davila.campusgps.data.UbicacionRepositoryGoogle
import com.davila.campusgps.data.campusGpsDataStore
import com.davila.campusgps.ui.PantallaMapa
import com.davila.campusgps.viewmodel.CampusViewModelFactory
import com.davila.campusgps.viewmodel.MapaViewModel
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    private val mapaViewModel: MapaViewModel by viewModels {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        CampusViewModelFactory(
            ubicacionRepository = UbicacionRepositoryGoogle(applicationContext, fusedLocationClient),
            lugaresRepository = LugaresRepositoryLocal(),
            ultimoLugarVisitadoRepository = UltimoLugarVisitadoRepositoryDataStore(
                applicationContext.campusGpsDataStore,
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PantallaMapa(mapaViewModel)
            }
        }
    }
}
