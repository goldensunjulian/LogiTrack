package com.example.LogiTrack.dto.response;

// El password nunca se expone en el response aunque esté hasheado.
public record CredencialesDtoResponse(
        Long id,
        String username,
        UsuarioDtoResponse usuario,
        RolDtoResponse rol
) {
}
