package com.example.LogiTrack.dto.response;

public record UsuarioDtoResponse(
        Long id, String nombre, String email, Boolean activo
) {
}
