package com.davila.avisoscampus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.davila.avisoscampus.model.Aviso
import com.davila.avisoscampus.model.Origen

@Entity(tableName = "avisos")
data class AvisoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val texto: String,
    val origen: String,
    val hora: String
) {
    fun aModelo() = Aviso(id, titulo, texto, Origen.valueOf(origen), hora)
}
