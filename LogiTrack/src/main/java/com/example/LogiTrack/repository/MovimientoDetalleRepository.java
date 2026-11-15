package com.example.LogiTrack.repository;

import com.example.LogiTrack.modelo.MovimientoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoDetalleRepository extends JpaRepository<MovimientoDetalle, Long> {
    // ver todos los productos que se movieron en un movimiento específico
    List<MovimientoDetalle> findByMovimientoId(Long movimientoId);

    // ver en qué movimientos aparece un producto específico
    List<MovimientoDetalle> findByProductoId(Long productoId);

    // productos más movidos en cantidad total
    // para el reporte resumen general
    @Query("SELECT md.producto.nombre, SUM(md.cantidad) FROM MovimientoDetalle md GROUP BY md.producto.id, md.producto.nombre ORDER BY SUM(md.cantidad) DESC")
    List<Object[]> productosMasMovidos();
}
