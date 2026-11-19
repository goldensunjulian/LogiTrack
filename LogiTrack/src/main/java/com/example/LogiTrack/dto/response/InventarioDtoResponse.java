package com.example.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record InventarioDtoResponse(
        @Schema(description = "ID único del registro de inventario", example = "1")
        Long id,

        @Schema(description = "Bodega donde se almacena el producto")
        BodegaDtoResponse bodega,

        @Schema(description = "Producto almacenado")
        ProductoDtoResponse producto,

        @Schema(description = "Cantidad actual en stock", example = "100")
        Integer stock
) {
}