package com.example.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioDtoRequest(
        @Schema()
        @NotBlank
        String nombre,
        @Schema
        @NotBlank
        String email,
        @Schema
        @NotNull
        Boolean activo
) {
}
