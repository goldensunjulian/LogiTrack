package com.example.LogiTrack.dto.response;

public record MovimientoDetalleDtoResponse(
        Long id,
        MovimientoDtoResponse movimiento,
        ProductoDtoResponse producto,
        Integer cantidad
) {
}
