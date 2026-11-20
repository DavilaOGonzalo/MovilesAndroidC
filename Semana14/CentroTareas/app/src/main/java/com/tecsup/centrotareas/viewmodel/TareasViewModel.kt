package com.tecsup.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.centrotareas.data.TareasRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje = _mensaje.asStateFlow()
    fun tareaLarga() {
        viewModelScope.launch {
            _mensaje.value = "Trabajando..."
            Log.d("HILO", "Antes: ${Thread.currentThread().name}")
            withContext(Dispatchers.Default) { Log.d("HILO", "Dentro: ${Thread.currentThread().name}"); delay(4_000) }
            Log.d("HILO", "Después: ${Thread.currentThread().name}")
            _mensaje.value = "Terminó"
        }
    }
}
