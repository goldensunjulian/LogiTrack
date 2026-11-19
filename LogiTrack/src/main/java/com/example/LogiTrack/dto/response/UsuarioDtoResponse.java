package com.example.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UsuarioDtoResponse(
        @Schema(description = "ID único del usuario", example = "1")
        Long id,

        @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
        String nombre,

        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@logitrack.com")
        String email,

        @Schema(description = "Indica si el usuario está activo", example = "true")
        Boolean activo
) {
}