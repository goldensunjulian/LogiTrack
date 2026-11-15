package com.example.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductoDtoRequest(
        @Schema(description = "Nombre del producto", example = "Monitor 24 pulgadas")
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String nombre,

        @Schema(description = "ID de la categoría a la que pertenece el producto", example = "1")
        @NotNull(message = "La categoría no puede ser nula")
        Long categoriaId,

        @Schema(description = "Precio unitario del producto", example = "350000.00")
        @NotNull(message = "El precio no puede ser nulo")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @Schema(description = "Estado activo o inactivo del producto", example = "true")
        @NotNull(message = "El estado activo no puede ser nulo")
        Boolean activo
) {
}
