package com.davila.avisoscampus.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AvisoEntity::class], version = 1, exportSchema = false)
abstract class AvisosDatabase : RoomDatabase() {
    abstract fun avisosDao(): AvisosDao
}
