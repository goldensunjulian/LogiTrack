package com.example.LogiTrack.dto.response;

import java.math.BigDecimal;

public record ProductoDtoResponse(
        Long id,
        String nombre,
        CategoriaDtoResponse categoria,
        BigDecimal precio,
        Boolean activo
) {
}
