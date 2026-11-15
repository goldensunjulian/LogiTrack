package com.example.LogiTrack.dto.response;

import com.example.LogiTrack.modelo.TipoOperacion;

import java.time.LocalDateTime;

public record AuditoriaDtoResponse(
        Long id,
        TipoOperacion operacion,
        LocalDateTime fecha,
        UsuarioDtoResponse usuario,
        String entidad,
        Long entidadId,
        String valoresAntes,
        String valoresDespues
) {
}
