package com.example.LogiTrack.controller;

import com.example.LogiTrack.dto.request.BodegaDtoRequest;
import com.example.LogiTrack.dto.response.BodegaDtoResponse;
import com.example.LogiTrack.service.BodegaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gestión de bodegas", description = "API para registrar, consultar, actualizar y desactivar bodegas")
@RestController
@RequestMapping("/api/bodega")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    @Operation(summary = "Registrar una bodega", description = "Crea una nueva bodega en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bodega registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o bodega ya existente")
    })
    @PostMapping
    public ResponseEntity<BodegaDtoResponse> guardar(@Valid @RequestBody BodegaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bodegaService.guardar(dto));
    }

    @Operation(summary = "Listar todas las bodegas", description = "Retorna todas las bodegas activas e inactivas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<BodegaDtoResponse>> listar() {
        return ResponseEntity.ok(bodegaService.listar());
    }

    @Operation(summary = "Listar bodegas activas", description = "Retorna solo las bodegas con estado activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/activas")
    public ResponseEntity<List<BodegaDtoResponse>> listarActivas() {
        return ResponseEntity.ok(bodegaService.listarActivas());
    }

    @Operation(summary = "Buscar bodega por ID", description = "Retorna una bodega específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BodegaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.buscarId(id));
    }

    @Operation(summary = "Actualizar bodega", description = "Modifica los datos de una bodega existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BodegaDtoResponse> actualizar(@Valid @RequestBody BodegaDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.actualizar(dto, id));
    }

    @Operation(summary = "Desactivar bodega", description = "Marca la bodega como inactiva sin borrarla de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bodega desactivada correctamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bodegaService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Activar bodega", description = "Reactiva una bodega que estaba inactiva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bodega activada correctamente"),
            @ApiResponse(responseCode = "404", description = "Bodega no encontrada")
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        bodegaService.activar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
