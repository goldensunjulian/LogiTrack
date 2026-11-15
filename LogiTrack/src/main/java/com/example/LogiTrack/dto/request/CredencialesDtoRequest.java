package com.example.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CredencialesDtoRequest(
        @Schema(description = "Nombre de usuario para iniciar sesión", example = "jperez")
        @NotBlank(message = "El username no puede estar vacío")
        @Size(max = 50, message = "El username no puede superar 50 caracteres")
        String username,

        @Schema(description = "Contraseña del usuario", example = "Segura123*")
        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(max = 225, message = "La contraseña no puede superar 225 caracteres")
        String password,

        @Schema(description = "ID del usuario al que pertenecen estas credenciales", example = "1")
        @NotNull(message = "El ID del usuario no puede ser nulo")
        Long usuarioId,

        @Schema(description = "ID del rol asignado al usuario", example = "1")
        @NotNull(message = "El ID del rol no puede ser nulo")
        Long rolId
) {
}
