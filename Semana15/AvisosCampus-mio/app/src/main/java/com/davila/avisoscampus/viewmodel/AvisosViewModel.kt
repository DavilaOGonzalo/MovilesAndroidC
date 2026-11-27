package com.davila.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import com.davila.avisoscampus.data.AvisosRepository
import com.davila.avisoscampus.model.Aviso
import com.davila.avisoscampus.model.PermisoUi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {
    val avisos: StateFlow<List<Aviso>> = repo.avisos
    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()
    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // TODO Paso 2: fun permisoRespondido(concedido: Boolean)
    // TODO Paso 3: fun avisarAhora()
    // TODO Paso 5: faltan, recordarEn(segundos) y cancelarRecordatorio()
    // TODO Paso 6: escuchandoAvion, cambiarEscucha(valor) y alCambiarModoAvion(activo)
    // TODO Paso 8: fun noSePudoMostrar()
}
