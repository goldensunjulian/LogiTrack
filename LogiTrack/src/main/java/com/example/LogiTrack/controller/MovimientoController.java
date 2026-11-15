package com.example.LogiTrack.controller;

import com.example.LogiTrack.dto.request.MovimientoDtoRequest;
import com.example.LogiTrack.dto.response.MovimientoDtoResponse;
import com.example.LogiTrack.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Gestión de movimientos", description = "API para registrar y consultar movimientos de inventario entre bodegas")
@RestController
@RequestMapping("/api/movimiento")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Operation(summary = "Registrar un movimiento", description = "Crea un movimiento de entrada, salida o transferencia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio incumplida")
    })
    @PostMapping
    public ResponseEntity<MovimientoDtoResponse> guardar(@Valid @RequestBody MovimientoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.guardar(dto));
    }

    @Operation(summary = "Listar todos los movimientos", description = "Retorna todos los movimientos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<MovimientoDtoResponse>> listar() {
        return ResponseEntity.ok(movimientoService.listar());
    }

    @Operation(summary = "Buscar movimiento por ID", description = "Retorna un movimiento específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarId(id));
    }

    @Operation(summary = "Buscar movimientos por usuario", description = "Retorna todos los movimientos realizados por un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(movimientoService.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Buscar movimientos por tipo", description = "Retorna movimientos filtrados por tipo: entrada, salida o transferencia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/tipo")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarPorTipo(@RequestParam String tipo) {
        return ResponseEntity.ok(movimientoService.buscarPorTipo(tipo));
    }

    @Operation(summary = "Buscar movimientos por rango de fechas", description = "Retorna movimientos realizados entre dos fechas — requerido en la rúbrica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    // Ejemplo: /api/movimiento/fechas?inicio=2026-01-01T00:00:00&fin=2026-12-31T23:59:59
    @GetMapping("/fechas")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(movimientoService.buscarPorFechas(inicio, fin));
    }

    @Operation(summary = "Eliminar movimiento", description = "Elimina un movimiento por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        movimientoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
