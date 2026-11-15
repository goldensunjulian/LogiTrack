package com.example.LogiTrack.dto.response;

import com.example.LogiTrack.modelo.TipoMovimiento;

import java.time.LocalDateTime;

public record MovimientoDtoResponse(
        Long id,
        LocalDateTime fecha,
        TipoMovimiento tipoMovimiento,
        UsuarioDtoResponse usuario,
        BodegaDtoResponse bodegaOrigen,
        BodegaDtoResponse bodegaDestino
) {
}
