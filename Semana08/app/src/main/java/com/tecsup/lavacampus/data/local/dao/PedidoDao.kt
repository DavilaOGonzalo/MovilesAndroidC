package com.tecsup.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.tecsup.lavacampus.data.local.entity.Pedido
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(pedido: Pedido): Long

    @Query("UPDATE pedidos SET estado = :estado, casillero = :casillero WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String, casillero: String?)

    @Query("UPDATE pedidos SET pagado = :pagado WHERE id = :id")
    suspend fun actualizarPagado(id: Int, pagado: Boolean)

    @Query("SELECT * FROM pedidos WHERE id = :id LIMIT 1")
    suspend fun obtener(id: Int): Pedido?

    @Transaction
    @Query("SELECT * FROM pedidos WHERE usuarioId = :usuarioId ORDER BY id DESC")
    fun observarPorUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos ORDER BY id DESC")
    fun observarTodos(): Flow<List<PedidoConDetalle>>

    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :id LIMIT 1")
    fun observarUno(id: Int): Flow<PedidoConDetalle?>

    @Query(
        """
        SELECT COUNT(*) FROM pedidos
        WHERE usuarioId = :usuarioId
          AND estado IN ('RECIBIDO', 'EN_PROCESO', 'LISTO')
        """
    )
    suspend fun contarActivos(usuarioId: Int): Int

    @Query("SELECT casillero FROM pedidos WHERE estado = 'LISTO' AND casillero IS NOT NULL")
    fun observarCasillerosOcupados(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM pedidos WHERE fechaRegistro = :fecha AND estado != 'CANCELADO'")
    fun contarPorFecha(fecha: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    @Query(
        """
        SELECT COALESCE(SUM(total), 0.0) FROM pedidos
        WHERE fechaRegistro >= :fechaInicio AND pagado = 1
        """
    )
    fun ingresosDesde(fechaInicio: String): Flow<Double>

    @Query(
        """
        SELECT id FROM pedidos
        WHERE fechaEntrega < :fechaHoy
          AND estado NOT IN ('ENTREGADO', 'CANCELADO')
        ORDER BY fechaEntrega
        """
    )
    fun observarAtrasados(fechaHoy: String): Flow<List<Int>>
}
