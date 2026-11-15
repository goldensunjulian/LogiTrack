package com.example.LogiTrack.controller;

import com.example.LogiTrack.dto.request.InventarioDtoRequest;
import com.example.LogiTrack.dto.response.InventarioDtoResponse;
import com.example.LogiTrack.service.InventarioService;
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
import java.util.Map;

@Tag(name = "Gestión de inventario", description = "API para registrar, consultar y actualizar el inventario por bodega")
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @Operation(summary = "Registrar inventario", description = "Crea un registro de stock para un producto en una bodega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventario registrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Bodega o producto no encontrado")
    })
    @PostMapping
    public ResponseEntity<InventarioDtoResponse> guardar(@Valid @RequestBody InventarioDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.guardar(dto));
    }

    @Operation(summary = "Listar todo el inventario", description = "Retorna todos los registros de inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<InventarioDtoResponse>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @Operation(summary = "Buscar inventario por ID", description = "Retorna un registro de inventario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarId(id));
    }

    @Operation(summary = "Buscar inventario por bodega", description = "Retorna todos los productos con su stock en una bodega específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/bodega/{bodegaId}")
    public ResponseEntity<List<InventarioDtoResponse>> buscarPorBodega(@PathVariable Long bodegaId) {
        return ResponseEntity.ok(inventarioService.buscarPorBodega(bodegaId));
    }

    @Operation(summary = "Productos con stock bajo", description = "Retorna productos con menos de 10 unidades en cualquier bodega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/stockBajo")
    public ResponseEntity<List<InventarioDtoResponse>> stockBajo() {
        return ResponseEntity.ok(inventarioService.stockBajo());
    }

    @Operation(summary = "Stock total por bodega", description = "Reporte resumen con la cantidad total de unidades por bodega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente")
    })
    @GetMapping("/reporte/stockPorBodega")
    public ResponseEntity<List<Map<String, Object>>> stockTotalPorBodega() {
        return ResponseEntity.ok(inventarioService.stockTotalPorBodega());
    }

    @Operation(summary = "Actualizar inventario", description = "Modifica el stock de un registro de inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventarioDtoResponse> actualizar(@Valid @RequestBody InventarioDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.actualizar(dto, id));
    }

    @Operation(summary = "Eliminar inventario", description = "Elimina un registro de inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
