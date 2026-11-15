package com.example.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MovimientoDetalleDtoRequest(
        @Schema(description = "ID del movimiento al que pertenece este detalle", example = "1")
        @NotNull(message = "El ID del movimiento no puede ser nulo")
        Long movimientoId,

        @Schema(description = "ID del producto involucrado en el movimiento", example = "3")
        @NotNull(message = "El ID del producto no puede ser nulo")
        Long productoId,

        @Schema(description = "Cantidad de unidades del producto en este movimiento", example = "10")
        @NotNull(message = "La cantidad no puede ser nula")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer cantidad
) {
}
