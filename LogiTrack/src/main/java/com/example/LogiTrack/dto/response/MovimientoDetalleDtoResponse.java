package com.example.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MovimientoDetalleDtoResponse(
        @Schema(description = "ID único del detalle de movimiento", example = "1")
        Long id,

        @Schema(description = "Movimiento al que pertenece el detalle")
        MovimientoDtoResponse movimiento,

        @Schema(description = "Producto involucrado en el movimiento")
        ProductoDtoResponse producto,

        @Schema(description = "Cantidad de unidades movidas", example = "10")
        Integer cantidad
) {
}