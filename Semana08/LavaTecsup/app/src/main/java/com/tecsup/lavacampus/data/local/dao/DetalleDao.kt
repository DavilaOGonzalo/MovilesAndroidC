package com.tecsup.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.lavacampus.data.local.entity.DetallePedido
import com.tecsup.lavacampus.model.IngresoServicio
import kotlinx.coroutines.flow.Flow

@Dao
interface DetalleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(lista: List<DetallePedido>)

    @Query("SELECT COUNT(*) FROM detalles_pedido WHERE servicioId = :servicioId")
    suspend fun contarPorServicio(servicioId: Int): Int

    @Query(
        """
        SELECT s.nombre AS nombre,
               COALESCE(SUM(d.subtotal), 0.0) AS total
        FROM detalles_pedido d
        INNER JOIN servicios s ON d.servicioId = s.id
        INNER JOIN pedidos p ON d.pedidoId = p.id
        WHERE p.pagado = 1 AND p.fechaRegistro >= :fechaInicio
        GROUP BY s.id
        ORDER BY total DESC
        """
    )
    fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>>
}
