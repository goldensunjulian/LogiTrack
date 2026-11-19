package com.example.LogiTrack.dto.response;

import com.example.LogiTrack.modelo.TipoOperacion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record AuditoriaDtoResponse(
        @Schema(description = "ID único del registro de auditoría", example = "1")
        Long id,

        @Schema(description = "Tipo de operación realizada", example = "UPDATE")
        TipoOperacion operacion,

        @Schema(description = "Fecha y hora en que ocurrió la operación", example = "2026-11-18T14:30:00")
        LocalDateTime fecha,

        @Schema(description = "Usuario que ejecutó la operación")
        UsuarioDtoResponse usuario,

        @Schema(description = "Nombre de la entidad afectada", example = "Producto")
        String entidad,

        @Schema(description = "ID del registro afectado", example = "5")
        Long entidadId,

        @Schema(description = "Valores anteriores del registro en formato JSON", example = "{\"precio\": 300000}")
        String valoresAntes,

        @Schema(description = "Valores nuevos del registro en formato JSON", example = "{\"precio\": 350000}")
        String valoresDespues
) {
}