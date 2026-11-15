package com.example.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioDtoRequest(
        @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String nombre,

        @Schema(description = "Correo electrónico único del usuario", example = "juan.perez@logitrack.com")
        @NotBlank(message = "El email no puede estar vacío")
        @Size(max = 225, message = "El email no puede superar 225 caracteres")
        String email,

        @Schema(description = "Estado activo o inactivo del usuario", example = "true")
        @NotNull(message = "El estado activo no puede ser nulo")
        Boolean activo
) {
}
