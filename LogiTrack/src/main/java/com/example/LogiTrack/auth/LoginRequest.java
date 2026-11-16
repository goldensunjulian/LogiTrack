package com.example.LogiTrack.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "Nombre de usuario", example = "admin")
        @NotBlank(message = "El username no puede estar vacío")
        String username,

        @Schema(description = "Contraseña del usuario", example = "1234")
        @NotBlank(message = "La contraseña no puede estar vacía")
        String password
) {
}
