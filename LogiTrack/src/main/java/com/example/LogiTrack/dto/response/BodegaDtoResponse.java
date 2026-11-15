package com.example.LogiTrack.dto.response;

public record BodegaDtoResponse(
        Long id,
        String nombre,
        String ciudad,
        String direccion,
        Integer capacidad,
        UsuarioDtoResponse usuarioEncargado,
        Boolean activado
) {
}
