package com.example.LogiTrack.dto.response;

import com.example.LogiTrack.modelo.TipoMovimiento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record MovimientoDtoResponse(
        @Schema(description = "ID único del movimiento", example = "1")
        Long id,

        @Schema(description = "Fecha y hora del movimiento", example = "2026-11-18T14:30:00")
        LocalDateTime fecha,

        @Schema(description = "Tipo de movimiento realizado", example = "entrada")
        TipoMovimiento tipoMovimiento,

        @Schema(description = "Usuario que registró el movimiento")
        UsuarioDtoResponse usuario,

        @Schema(description = "Bodega de origen (aplica para salida o transferencia)")
        BodegaDtoResponse bodegaOrigen,

        @Schema(description = "Bodega de destino (aplica para entrada o transferencia)")
        BodegaDtoResponse bodegaDestino
) {
}