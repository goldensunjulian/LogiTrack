package com.example.LogiTrack.dto.request;

import com.example.LogiTrack.modelo.TipoMovimiento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MovimientoDtoRequest(
        @Schema(description = "Tipo de movimiento", example = "entrada")
        @NotNull(message = "El tipo de movimiento no puede ser nulo")
        TipoMovimiento tipoMovimiento,

        @Schema(description = "ID del usuario que registra el movimiento", example = "2")
        @NotNull(message = "El ID del usuario no puede ser nulo")
        Long usuarioId,

        @Schema(description = "ID de la bodega origen (requerido para salida y transferencia, nulo para entrada)", example = "1")
        Long bodegaOrigenId,

        @Schema(description = "ID de la bodega destino (requerido para entrada y transferencia, nulo para salida)", example = "2")
        Long bodegaDestinoId
) {
}
