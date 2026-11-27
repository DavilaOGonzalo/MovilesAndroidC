package com.davila.avisoscampus

import android.app.Application
import com.davila.avisoscampus.notificacion.Notificador
import androidx.room.Room
import com.davila.avisoscampus.data.AvisosRepositoryRoom
import com.davila.avisoscampus.data.local.AvisosDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AvisosApp : Application() {
    private val db by lazy { Room.databaseBuilder(this, AvisosDatabase::class.java, "avisos.db").build() }
    val repository by lazy {
        AvisosRepositoryRoom(db.avisosDao(), CoroutineScope(SupervisorJob() + Dispatchers.IO))
    }

    override fun onCreate() {
        super.onCreate()
        Notificador.crearCanal(this)
    }
}
