package com.davila.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davila.campusgps.data.LugaresRepository
import com.davila.campusgps.data.UbicacionRepository
import com.davila.campusgps.data.UltimoLugarVisitadoRepository

class CampusViewModelFactory(
    private val ubicacionRepository: UbicacionRepository,
    private val lugaresRepository: LugaresRepository,
    private val ultimoLugarVisitadoRepository: UltimoLugarVisitadoRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapaViewModel::class.java)) {
            return MapaViewModel(
                ubicacionRepository,
                lugaresRepository,
                ultimoLugarVisitadoRepository,
            ) as T
        }
        throw IllegalArgumentException("ViewModel no soportado: ${modelClass.name}")
    }
}
