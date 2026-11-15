package com.example.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BodegaDtoRequest(
        @Schema(description = "Nombre de la bodega", example = "Bodega Norte")
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String nombre,

        @Schema(description = "Ciudad donde está ubicada la bodega", example = "Bogotá")
        @NotBlank(message = "La ciudad no puede estar vacía")
        @Size(max = 100, message = "La ciudad no puede superar 100 caracteres")
        String ciudad,

        @Schema(description = "Dirección exacta de la bodega", example = "Calle 80 #45-23")
        @NotBlank(message = "La dirección no puede estar vacía")
        @Size(max = 150, message = "La dirección no puede superar 150 caracteres")
        String direccion,

        @Schema(description = "Capacidad máxima de almacenamiento", example = "500")
        @NotNull(message = "La capacidad no puede ser nula")
        @Min(value = 1, message = "La capacidad debe ser mayor a 0")
        Integer capacidad,

        @Schema(description = "ID del usuario encargado de la bodega (puede ser nulo)", example = "1")
        Long usuarioEncargadoId
) {
}
