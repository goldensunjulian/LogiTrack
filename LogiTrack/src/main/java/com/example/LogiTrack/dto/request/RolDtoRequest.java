package com.example.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RolDtoRequest(
        @Schema(description = "Nombre del rol", example = "ADMIN")
        @NotBlank(message = "El nombre del rol no puede estar vacío")
        @Size(max = 50, message = "El nombre del rol no puede superar 50 caracteres")
        String nombre
) {
}
