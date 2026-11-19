package com.example.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record BodegaDtoResponse(
        @Schema(description = "ID único de la bodega", example = "1")
        Long id,

        @Schema(description = "Nombre de la bodega", example = "Bodega Norte")
        String nombre,

        @Schema(description = "Ciudad donde se ubica la bodega", example = "Bogotá")
        String ciudad,

        @Schema(description = "Dirección física de la bodega", example = "Calle 80 #45-23")
        String direccion,

        @Schema(description = "Capacidad máxima de almacenamiento", example = "500")
        Integer capacidad,

        @Schema(description = "Usuario encargado de la bodega")
        UsuarioDtoResponse usuarioEncargado,

        @Schema(description = "Indica si la bodega está activa", example = "true")
        Boolean activado
) {
}