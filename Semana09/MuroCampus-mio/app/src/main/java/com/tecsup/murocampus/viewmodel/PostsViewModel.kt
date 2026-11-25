package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.ensureActive

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()
    private var cargaLista: Job? = null

    private val _idSeleccionado = MutableStateFlow<Int?>(null)
    val idSeleccionado = _idSeleccionado.asStateFlow()
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()
    private var cargaDetalle: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        cargaLista?.cancel()
        cargaLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            repo.listar()
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun abrir(id: Int) {
        cargaDetalle?.cancel()
        _detalle.value = UiState.Cargando
        _idSeleccionado.value = id
        cargaDetalle = viewModelScope.launch {
            val post = repo.detalle(id).getOrElse {
                ensureActive()
                _detalle.value = UiState.Error(mensajeDe(it))
                return@launch
            }
            val comentarios = repo.comentarios(id)
            ensureActive()
            _detalle.value = comentarios.fold(
                onSuccess = { UiState.Exito(DetalleUi(post, it)) },
                onFailure = { UiState.Error(mensajeDe(it)) },
            )
        }
    }

    fun reintentarDetalle() {
        _idSeleccionado.value?.let(::abrir)
    }

    fun cerrarDetalle() {
        cargaDetalle?.cancel()
        _idSeleccionado.value = null
        _detalle.value = UiState.Cargando
    }
}
