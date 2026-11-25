package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
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
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    private val _idSeleccionado = MutableStateFlow<Int?>(null)
    val idSeleccionado = _idSeleccionado.asStateFlow()
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()
    private var cargaDetalle: Job? = null
    private val idsEliminados = mutableSetOf<Int>()
    private val _eliminacion = MutableStateFlow<UiState<Unit>?>(null)
    val eliminacion = _eliminacion.asStateFlow()

    private val _publicacion = MutableStateFlow<UiState<Post>?>(null)
    val publicacion = _publicacion.asStateFlow()
    private var envioPublicacion: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        cargaLista?.cancel()
        val usuarioId = _usuarioFiltro.value
        cargaLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val respuesta = if (usuarioId == null) repo.listar() else repo.porUsuario(usuarioId)
            ensureActive()
            _lista.value = respuesta.fold(
                onSuccess = { UiState.Exito(it.filterNot { post -> post.id in idsEliminados }) },
                onFailure = { UiState.Error(mensajeDe(it)) },
            )
        }
    }

    fun filtrar(userId: Int?) {
        if (_usuarioFiltro.value == userId) return
        _usuarioFiltro.value = userId
        cargar()
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
        if (_eliminacion.value == UiState.Cargando) return
        if (_eliminacion.value is UiState.Error) _eliminacion.value = null
        cargaDetalle?.cancel()
        _idSeleccionado.value = null
        _detalle.value = UiState.Cargando
    }

    fun eliminar() {
        val id = _idSeleccionado.value ?: return
        if (_detalle.value !is UiState.Exito || _eliminacion.value == UiState.Cargando) return
        _eliminacion.value = UiState.Cargando
        viewModelScope.launch {
            val respuesta = repo.eliminar(id)
            ensureActive()
            respuesta.onSuccess {
                idsEliminados.add(id)
                val listaActual = _lista.value
                if (listaActual is UiState.Exito) {
                    _lista.value = UiState.Exito(listaActual.datos.filterNot { it.id == id })
                }
                _eliminacion.value = UiState.Exito(Unit)
                cerrarDetalle()
            }.onFailure {
                _eliminacion.value = UiState.Error(mensajeDe(it))
            }
        }
    }

    fun consumirAvisoEliminacion() {
        if (_eliminacion.value is UiState.Exito) _eliminacion.value = null
    }

    fun publicar(titulo: String, texto: String) {
        if (titulo.isBlank() || texto.isBlank() || _publicacion.value == UiState.Cargando) return
        _publicacion.value = UiState.Cargando
        envioPublicacion = viewModelScope.launch {
            val respuesta = repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
            ensureActive()
            _publicacion.value = respuesta.fold(
                onSuccess = { UiState.Exito(it) },
                onFailure = { UiState.Error(mensajeDe(it)) },
            )
        }
    }

    fun limpiarPublicacion() {
        envioPublicacion?.cancel()
        _publicacion.value = null
    }
}
