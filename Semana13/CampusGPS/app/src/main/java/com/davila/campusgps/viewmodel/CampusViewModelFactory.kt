package com.davila.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davila.campusgps.data.LugaresRepository
import com.davila.campusgps.data.UbicacionRepository

class CampusViewModelFactory(
    private val ubicacionRepository: UbicacionRepository,
    private val lugaresRepository: LugaresRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapaViewModel::class.java)) {
            return MapaViewModel(ubicacionRepository, lugaresRepository) as T
        }
        throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
    }
}
