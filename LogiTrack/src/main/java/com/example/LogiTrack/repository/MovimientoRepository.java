package com.example.LogiTrack.repository;

import com.example.LogiTrack.modelo.Movimiento;
import com.example.LogiTrack.modelo.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    // ver todos los movimientos que hizo un usuario específico
    List<Movimiento> findByUsuarioId(Long usuarioId);

    // ver movimientos por tipo (entrada, salida, transferencia)
    List<Movimiento> findByTipoMovimiento(TipoMovimiento tipoMovimiento);

    // ver movimientos de una bodega origen específica
    List<Movimiento> findByBodegaOrigenId(Long bodegaId);

    // ver movimientos de una bodega destino específica
    List<Movimiento> findByBodegaDestinoId(Long bodegaId);

    // Movimientos por rango de fechas
    List<Movimiento> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
