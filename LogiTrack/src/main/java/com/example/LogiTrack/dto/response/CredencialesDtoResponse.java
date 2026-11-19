package com.example.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

// El password nunca se expone en el response aunque esté hasheado.
public record CredencialesDtoResponse(
        @Schema(description = "ID único de las credenciales", example = "1")
        Long id,

        @Schema(description = "Nombre de usuario para iniciar sesión", example = "jperez")
        String username,

        @Schema(description = "Datos del usuario asociado")
        UsuarioDtoResponse usuario,

        @Schema(description = "Rol asignado al usuario")
        RolDtoResponse rol
) {
}