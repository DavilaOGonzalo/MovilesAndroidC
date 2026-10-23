package com.tecsup.lavacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"

    const val INICIO = "estudiante/inicio"
    const val MIS_PEDIDOS = "estudiante/pedidos"
    const val TARIFAS = "estudiante/tarifas"
    const val PERFIL = "estudiante/perfil"
    const val NUEVO_PEDIDO = "estudiante/nuevo?repeatId={repeatId}"
    const val DETALLE = "estudiante/detalle/{id}"

    const val PANEL = "admin/panel"
    const val BANDEJA = "admin/pedidos"
    const val SERVICIOS = "admin/servicios"
    const val CLIENTES = "admin/clientes"
    const val AJUSTES = "admin/ajustes"
    const val FORMULARIO_SERVICIO = "admin/servicio?id={id}"
    const val GESTION = "admin/gestion/{id}"

    fun nuevoPedido(repetirId: Int = -1) = "estudiante/nuevo?repeatId=$repetirId"
    fun detalle(id: Int) = "estudiante/detalle/$id"
    fun formularioServicio(id: Int = -1) = "admin/servicio?id=$id"
    fun gestion(id: Int) = "admin/gestion/$id"
}
