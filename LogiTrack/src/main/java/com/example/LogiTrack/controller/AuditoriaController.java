package com.example.LogiTrack.controller;

import com.example.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.example.LogiTrack.modelo.TipoOperacion;
import com.example.LogiTrack.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Auditoría", description = "API para consultar el historial de cambios del sistema — solo lectura")
@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @Operation(summary = "Listar todas las auditorías", description = "Retorna el historial completo de cambios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<AuditoriaDtoResponse>> listar() {
        return ResponseEntity.ok(auditoriaService.listar());
    }

    @Operation(summary = "Buscar auditoría por ID", description = "Retorna un registro de auditoría específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarId(id));
    }

    @Operation(summary = "Buscar auditorías por usuario", description = "Retorna todos los cambios realizados por un usuario específico — requerido en la rúbrica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(auditoriaService.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Buscar auditorías por tipo de operación", description = "Retorna cambios filtrados por insert, update o delete — requerido en la rúbrica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    // Ejemplo: /api/auditoria/operacion?tipo=insert
    @GetMapping("/operacion")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarPorOperacion(@RequestParam TipoOperacion tipo) {
        return ResponseEntity.ok(auditoriaService.buscarPorTipoOperacion(tipo));
    }
}
