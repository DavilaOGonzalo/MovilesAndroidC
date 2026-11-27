package com.davila.avisoscampus.data

import com.davila.avisoscampus.model.Aviso
import com.davila.avisoscampus.model.Origen
import com.davila.avisoscampus.data.local.AvisoEntity
import com.davila.avisoscampus.data.local.AvisosDao
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

interface AvisosRepository {
    val avisos: StateFlow<List<Aviso>>
    suspend fun registrar(titulo: String, texto: String, origen: Origen): Aviso
}

class AvisosRepositoryRoom(private val dao: AvisosDao, scope: CoroutineScope) : AvisosRepository {
    override val avisos: StateFlow<List<Aviso>> = dao.observar()
        .map { entities -> entities.map(AvisoEntity::aModelo) }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override suspend fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val entity = AvisoEntity(titulo = titulo, texto = texto, origen = origen.name, hora = hora)
        val id = dao.insertar(entity).toInt()
        return Aviso(id, titulo, texto, origen, hora)
    }
}
