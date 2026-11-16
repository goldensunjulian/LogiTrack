package com.example.LogiTrack.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearEmpleadoRequest(
        @Schema(description = "Nombre completo del empleado", example = "Juan Pérez")
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String nombre,

        @Schema(description = "Correo electrónico del empleado", example = "juan.perez@logitrack.com")
        @NotBlank(message = "El email no puede estar vacío")
        String email,

        @Schema(description = "Nombre de usuario para iniciar sesión", example = "jperez")
        @NotBlank(message = "El username no puede estar vacío")
        @Size(max = 50, message = "El username no puede superar 50 caracteres")
        String username,

        @Schema(description = "Contraseña", example = "1234")
        @NotBlank(message = "La contraseña no puede estar vacía")
        String password,

        @Schema(description = "ID del rol a asignar", example = "1")
        @NotNull(message = "El rol no puede ser nulo")
        Long rolId
) {
}
