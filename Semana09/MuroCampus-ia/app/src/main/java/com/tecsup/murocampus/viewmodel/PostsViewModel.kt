package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.CambioTitulo
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

    private val titulosEditados = mutableMapOf<Int, String>()
    private val _editando = MutableStateFlow(false)
    val editando = _editando.asStateFlow()
    private val _edicion = MutableStateFlow<UiState<Post>?>(null)
    val edicion = _edicion.asStateFlow()

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
                onSuccess = { UiState.Exito(aplicarCambios(it)) },
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
                onSuccess = { UiState.Exito(DetalleUi(post.conTituloLocal(), it)) },
                onFailure = { UiState.Error(mensajeDe(it)) },
            )
        }
    }

    fun reintentarDetalle() {
        _idSeleccionado.value?.let(::abrir)
    }

    fun cerrarDetalle() {
        if (_eliminacion.value == UiState.Cargando || _editando.value) return
        if (_eliminacion.value is UiState.Error) _eliminacion.value = null
        cargaDetalle?.cancel()
        _idSeleccionado.value = null
        _detalle.value = UiState.Cargando
    }

    fun eliminar() {
        val id = _idSeleccionado.value ?: return
        if (_detalle.value !is UiState.Exito || _eliminacion.value == UiState.Cargando || _editando.value) {
            return
        }
        _eliminacion.value = UiState.Cargando
        viewModelScope.launch {
            val respuesta = repo.eliminar(id)
            ensureActive()
            respuesta.onSuccess {
                idsEliminados.add(id)
                titulosEditados.remove(id)
                reflejarCambiosEnLista()
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

    fun abrirEditor() {
        if (_detalle.value !is UiState.Exito || _eliminacion.value == UiState.Cargando) return
        _edicion.value = null
        _editando.value = true
    }

    fun cerrarEditor() {
        if (_edicion.value == UiState.Cargando) return
        _editando.value = false
        _edicion.value = null
    }

    fun editarTitulo(titulo: String) {
        val detalleActual = (_detalle.value as? UiState.Exito)?.datos ?: return
        val nuevoTitulo = titulo.trim()
        if (!_editando.value || _edicion.value == UiState.Cargando ||
            nuevoTitulo.isEmpty() || nuevoTitulo == detalleActual.post.title
        ) return

        _edicion.value = UiState.Cargando
        viewModelScope.launch {
            val respuesta = repo.editarTitulo(detalleActual.post.id, CambioTitulo(nuevoTitulo))
            ensureActive()
            respuesta.onSuccess { editado ->
                titulosEditados[detalleActual.post.id] = editado.title
                reflejarCambiosEnLista()
                _detalle.value = UiState.Exito(
                    detalleActual.copy(post = detalleActual.post.conTituloLocal()),
                )
                _edicion.value = UiState.Exito(editado)
            }.onFailure {
                _edicion.value = UiState.Error(mensajeDe(it))
            }
        }
    }

    fun limpiarAvisoEdicion() {
        if (_edicion.value != UiState.Cargando) _edicion.value = null
    }

    private fun aplicarCambios(posts: List<Post>): List<Post> =
        posts.filterNot { it.id in idsEliminados }.map { it.conTituloLocal() }

    private fun Post.conTituloLocal(): Post =
        titulosEditados[id]?.let { copy(title = it) } ?: this

    private fun reflejarCambiosEnLista() {
        val listaActual = _lista.value as? UiState.Exito ?: return
        _lista.value = UiState.Exito(aplicarCambios(listaActual.datos))
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
