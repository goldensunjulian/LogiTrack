package com.example.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoriaDtoResponse(
        @Schema(description = "ID único de la categoría", example = "1")
        Long id,

        @Schema(description = "Nombre de la categoría", example = "Electrónica")
        String nombre
) {
}