package com.example.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProductoDtoResponse(
        @Schema(description = "ID único del producto", example = "1")
        Long id,

        @Schema(description = "Nombre del producto", example = "Monitor 24 pulgadas")
        String nombre,

        @Schema(description = "Categoría a la que pertenece el producto")
        CategoriaDtoResponse categoria,

        @Schema(description = "Precio unitario del producto", example = "350000.00")
        BigDecimal precio,

        @Schema(description = "Indica si el producto está activo", example = "true")
        Boolean activo
) {
}