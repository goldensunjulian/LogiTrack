package com.example.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventarioDtoRequest(
        @Schema(description = "ID de la bodega", example = "1")
        @NotNull(message = "El ID de la bodega no puede ser nulo")
        Long bodegaId,

        @Schema(description = "ID del producto", example = "3")
        @NotNull(message = "El ID del producto no puede ser nulo")
        Long productoId,

        @Schema(description = "Cantidad de stock disponible en la bodega", example = "100")
        @NotNull(message = "El stock no puede ser nulo")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock
) {
}
