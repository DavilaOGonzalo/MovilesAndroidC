package com.tecsup.lavacampus.data.local

import androidx.room.withTransaction
import com.tecsup.lavacampus.data.local.entity.DetallePedido
import com.tecsup.lavacampus.data.local.entity.Pedido
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.Rol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate

object DatosIniciales {
    suspend fun sembrar(db: LavaDatabase) = withContext(Dispatchers.IO) {
        if (db.usuarioDao().contar() > 0) return@withContext

        db.servicioDao().insertarTodos(
            listOf(
                Servicio(
                    nombre = "Lavado y secado",
                    descripcion = "Lavado completo y secado de ropa",
                    unidad = "KG",
                    precio = 8.00,
                    horasEntrega = 24,
                    activo = true,
                    icono = "wash"
                ),
                Servicio(
                    nombre = "Planchado",
                    descripcion = "Planchado cuidadoso por prenda",
                    unidad = "PRENDA",
                    precio = 2.50,
                    horasEntrega = 48,
                    activo = true,
                    icono = "iron"
                ),
                Servicio(
                    nombre = "Lavado en seco",
                    descripcion = "Limpieza en seco para prendas delicadas",
                    unidad = "PRENDA",
                    precio = 12.00,
                    horasEntrega = 48,
                    activo = true,
                    icono = "shirt"
                ),
                Servicio(
                    nombre = "Edredón / frazada",
                    descripcion = "Limpieza de piezas grandes",
                    unidad = "UNIDAD",
                    precio = 20.00,
                    horasEntrega = 48,
                    activo = false,
                    icono = "shirt"
                )
            )
        )

        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(
                    nombre = "Ana García",
                    codigo = "U202310234",
                    correo = "ana@correo.test",
                    clave = "1234",
                    rol = Rol.CLIENTE.name
                ),
                Usuario(
                    nombre = "Luis Ramírez",
                    codigo = "U202310235",
                    correo = "luis@correo.test",
                    clave = "1234",
                    rol = Rol.CLIENTE.name
                ),
                Usuario(
                    nombre = "Marta Quispe",
                    codigo = "U202310236",
                    correo = "marta@correo.test",
                    clave = "1234",
                    rol = Rol.CLIENTE.name
                ),
                Usuario(
                    nombre = "Administración",
                    codigo = "",
                    correo = "admin@lavacampus.test",
                    clave = "admin",
                    rol = Rol.ADMIN.name
                )
            )
        )

        val lavado = db.servicioDao().obtenerPorNombre("Lavado y secado")!!
        val planchado = db.servicioDao().obtenerPorNombre("Planchado")!!
        val seco = db.servicioDao().obtenerPorNombre("Lavado en seco")!!
        val ana = db.usuarioDao().login("ana@correo.test", "1234")!!
        val luis = db.usuarioDao().login("luis@correo.test", "1234")!!
        val marta = db.usuarioDao().login("marta@correo.test", "1234")!!

        db.withTransaction {
            insertarPedido(
                db = db,
                usuarioId = ana.id,
                diasAtras = 1,
                estado = EstadoPedido.RECIBIDO,
                lineas = listOf(lavado to 2.0)
            )
            insertarPedido(
                db = db,
                usuarioId = ana.id,
                diasAtras = 2,
                estado = EstadoPedido.EN_PROCESO,
                lineas = listOf(lavado to 4.5, planchado to 2.0)
            )
            insertarPedido(
                db = db,
                usuarioId = ana.id,
                diasAtras = 3,
                estado = EstadoPedido.LISTO,
                casillero = "C-05",
                lineas = listOf(lavado to 2.0)
            )
            insertarPedido(
                db = db,
                usuarioId = ana.id,
                diasAtras = 7,
                estado = EstadoPedido.ENTREGADO,
                pagado = true,
                lineas = listOf(lavado to 3.0)
            )
            insertarPedido(
                db = db,
                usuarioId = ana.id,
                diasAtras = 10,
                estado = EstadoPedido.CANCELADO,
                lineas = listOf(seco to 1.0)
            )

            insertarPedido(
                db = db,
                usuarioId = luis.id,
                diasAtras = 1,
                estado = EstadoPedido.RECIBIDO,
                lineas = listOf(planchado to 5.0)
            )
            insertarPedido(
                db = db,
                usuarioId = luis.id,
                diasAtras = 4,
                estado = EstadoPedido.LISTO,
                casillero = "C-07",
                lineas = listOf(lavado to 2.0)
            )
            insertarPedido(
                db = db,
                usuarioId = luis.id,
                diasAtras = 6,
                estado = EstadoPedido.ENTREGADO,
                pagado = true,
                lineas = listOf(seco to 1.0)
            )

            insertarPedido(
                db = db,
                usuarioId = marta.id,
                diasAtras = 2,
                estado = EstadoPedido.EN_PROCESO,
                lineas = listOf(lavado to 6.0)
            )
            insertarPedido(
                db = db,
                usuarioId = marta.id,
                diasAtras = 8,
                estado = EstadoPedido.ENTREGADO,
                pagado = true,
                lineas = listOf(lavado to 1.5)
            )
        }
    }

    private suspend fun insertarPedido(
        db: LavaDatabase,
        usuarioId: Int,
        diasAtras: Long,
        estado: EstadoPedido,
        lineas: List<Pair<Servicio, Double>>,
        casillero: String? = null,
        pagado: Boolean = false
    ) {
        val registro = LocalDate.now().minusDays(diasAtras)
        val entrega = registro.plusDays(
            lineas.maxOf { (servicio, _) -> (servicio.horasEntrega + 23) / 24 }.toLong()
        ).let { fecha ->
            if (fecha.dayOfWeek == DayOfWeek.SUNDAY) fecha.plusDays(1) else fecha
        }
        val detalles = lineas.map { (servicio, cantidad) ->
            DetallePedido(
                pedidoId = 0,
                servicioId = servicio.id,
                cantidad = cantidad,
                precioUnitario = servicio.precio,
                subtotal = cantidad * servicio.precio
            )
        }
        val pedidoId = db.pedidoDao().insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = registro.toString(),
                fechaEntrega = entrega.toString(),
                estado = estado.name,
                total = detalles.sumOf { it.subtotal },
                casillero = casillero,
                pagado = pagado,
                nota = "Datos de demostración",
                fechaCreacion = registro.toEpochDay()
            )
        ).toInt()
        db.detalleDao().insertarTodos(detalles.map { it.copy(pedidoId = pedidoId) })
    }
}
