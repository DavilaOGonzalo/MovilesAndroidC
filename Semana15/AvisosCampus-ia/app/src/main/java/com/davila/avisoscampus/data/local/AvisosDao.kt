package com.davila.avisoscampus.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AvisosDao {
    @Query("SELECT * FROM avisos ORDER BY id DESC")
    fun observar(): Flow<List<AvisoEntity>>

    @Insert
    suspend fun insertar(aviso: AvisoEntity): Long
}
