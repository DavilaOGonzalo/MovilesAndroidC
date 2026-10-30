package com.tecsup.lavacampus.model

enum class EstadoPedido(
    val etiqueta: String,
    val colorHex: Long
) {
    RECIBIDO("Recibido", 0xFFD97706),
    EN_PROCESO("En proceso", 0xFF2563EB),
    LISTO("Listo", 0xFF16A34A),
    ENTREGADO("Entregado", 0xFF475569),
    CANCELADO("Cancelado", 0xFFDC2626);

    fun siguiente(): EstadoPedido? = when (this) {
        RECIBIDO -> EN_PROCESO
        EN_PROCESO -> LISTO
        LISTO -> ENTREGADO
        ENTREGADO, CANCELADO -> null
    }

    companion object {
        val activas = listOf(RECIBIDO, EN_PROCESO, LISTO)
        val panel = listOf(RECIBIDO, EN_PROCESO, LISTO, ENTREGADO)
    }
}
