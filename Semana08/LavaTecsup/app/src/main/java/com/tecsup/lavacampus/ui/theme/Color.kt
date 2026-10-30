package com.tecsup.lavacampus.ui.theme

import androidx.compose.ui.graphics.Color
import com.tecsup.lavacampus.model.EstadoPedido

val Turquesa = Color(0xFF0891B2)
val TurquesaContainer = Color(0xFFCFFAFE)
val TurquesaFondo = Color(0xFFF0FCFF)
val Violeta = Color(0xFF6D28D9)
val VioletaContainer = Color(0xFFEDE9FE)
val VioletaFondo = Color(0xFFFAF8FF)

val EstadoRecibido = Color(0xFFD97706)
val EstadoProceso = Color(0xFF2563EB)
val EstadoListo = Color(0xFF16A34A)
val EstadoEntregado = Color(0xFF475569)
val EstadoCancelado = Color(0xFFDC2626)

// Paleta nocturna: fondos profundos y acentos suaves para mantener una buena
// lectura sin perder la identidad turquesa/violeta de la aplicación.
val FondoNoche = Color(0xFF081719)
val SuperficieNoche = Color(0xFF102427)
val SuperficieVarianteNoche = Color(0xFF1D3639)
val TextoNoche = Color(0xFFE2F4F4)
val TextoSecundarioNoche = Color(0xFFB5CBCC)
val BordeNoche = Color(0xFF809A9B)
val BordeVarianteNoche = Color(0xFF3C5355)

val FondoNocheAdmin = Color(0xFF110E1B)
val SuperficieNocheAdmin = Color(0xFF1B1628)
val SuperficieVarianteNocheAdmin = Color(0xFF30273F)
val TextoNocheAdmin = Color(0xFFF3ECFF)
val TextoSecundarioNocheAdmin = Color(0xFFCAC0D3)
val BordeNocheAdmin = Color(0xFF958AA2)
val BordeVarianteNocheAdmin = Color(0xFF4B4253)

val TurquesaNoche = Color(0xFF72DFE2)
val TurquesaOnNoche = Color(0xFF003638)
val TurquesaContainerNoche = Color(0xFF004D50)
val TurquesaOnContainerNoche = Color(0xFF9BF3F4)

val VioletaNoche = Color(0xFFD0BCFF)
val VioletaOnNoche = Color(0xFF35105F)
val VioletaContainerNoche = Color(0xFF4B2D70)
val VioletaOnContainerNoche = Color(0xFFEFDBFF)

val SecundarioNoche = Color(0xFFB6CED0)
val SecundarioOnNoche = Color(0xFF1C3335)
val SecundarioContainerNoche = Color(0xFF2D4749)
val SecundarioOnContainerNoche = Color(0xFFD1E8EA)
val SecundarioAdminNoche = Color(0xFFDDC2FF)
val SecundarioAdminOnNoche = Color(0xFF381B5E)
val SecundarioAdminContainerNoche = Color(0xFF432B56)
val SecundarioAdminOnContainerNoche = Color(0xFFF2DAFF)

val TerciarioNoche = Color(0xFFA8DDB7)
val TerciarioOnNoche = Color(0xFF12351F)
val TerciarioContainerNoche = Color(0xFF28513A)
val TerciarioOnContainerNoche = Color(0xFFC3F2CA)

val ErrorNoche = Color(0xFFFFB4AB)
val ErrorOnNoche = Color(0xFF690005)
val ErrorContainerNoche = Color(0xFF93000A)
val ErrorOnContainerNoche = Color(0xFFFFDAD6)

val EstadoRecibidoNoche = Color(0xFFFFB86B)
val EstadoProcesoNoche = Color(0xFF8DBBFF)
val EstadoListoNoche = Color(0xFF87E5A0)
val EstadoEntregadoNoche = Color(0xFFB8C7D9)
val EstadoCanceladoNoche = Color(0xFFFF8F88)

fun EstadoPedido.colorEnTema(oscuro: Boolean): Color = if (!oscuro) {
    Color(colorHex)
} else {
    when (this) {
        EstadoPedido.RECIBIDO -> EstadoRecibidoNoche
        EstadoPedido.EN_PROCESO -> EstadoProcesoNoche
        EstadoPedido.LISTO -> EstadoListoNoche
        EstadoPedido.ENTREGADO -> EstadoEntregadoNoche
        EstadoPedido.CANCELADO -> EstadoCanceladoNoche
    }
}
