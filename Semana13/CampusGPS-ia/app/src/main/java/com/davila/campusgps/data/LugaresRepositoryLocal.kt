package com.davila.campusgps.data

import com.davila.campusgps.model.Lugar
import com.davila.campusgps.model.Posicion

class LugaresRepositoryLocal : LugaresRepository {
    // Coordenadas internas proporcionadas y confirmadas para CampusGPS.
    override fun obtenerLugares(): List<Lugar> = listOf(
        Lugar(
            1,
            "Biblioteca Central",
            "Estudio",
            posicion = Posicion(-12.044949, -76.952839),
        ),
        Lugar(
            2,
            "Cafetería Campus",
            "Comida",
            posicion = Posicion(-12.044944, -76.952389),
        ),
        Lugar(
            3,
            "Laboratorio de Cómputo",
            "Estudio",
            posicion = Posicion(-12.044444, -76.952889),
        ),
        Lugar(
            4,
            "Auditorio Principal",
            "Eventos",
            posicion = Posicion(-12.044824, -76.952685),
        ),
        Lugar(
            5,
            "Estacionamiento Norte",
            "Servicios",
            posicion = Posicion(-12.044408, -76.951787),
        ),
    )
}
