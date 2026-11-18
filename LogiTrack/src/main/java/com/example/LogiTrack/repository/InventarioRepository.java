package com.example.LogiTrack.repository;

import com.example.LogiTrack.modelo.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // mirar todo el inventario de una bodega
    List<Inventario> findByBodegaId(Long bodegaId);

    // buscar el registro exacto de un producto en una bodega 
    // sirve para actualizar stock en movimientos
    Inventario findByBodegaIdAndProductoId(Long bodegaId, Long productoId);

    // productos con stock bajo (menos de 10 unidades)
    @Query("SELECT i FROM Inventario i WHERE i.stock < 10")
    List<Inventario> findStockBajo();

    // stock total por bodega 
    // para el reporte resumen general
    @Query("SELECT i.bodega.nombre, SUM(i.stock) FROM Inventario i GROUP BY i.bodega.id, i.bodega.nombre")
    List<Object[]> stockTotalPorBodega();
}
