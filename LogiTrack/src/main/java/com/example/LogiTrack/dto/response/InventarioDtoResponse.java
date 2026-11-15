package com.example.LogiTrack.dto.response;

public record InventarioDtoResponse(
        Long id,
        BodegaDtoResponse bodega,
        ProductoDtoResponse producto,
        Integer stock
) {
}
