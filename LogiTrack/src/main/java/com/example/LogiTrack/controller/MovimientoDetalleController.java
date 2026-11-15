package com.example.LogiTrack.controller;

import com.example.LogiTrack.dto.request.MovimientoDetalleDtoRequest;
import com.example.LogiTrack.dto.response.MovimientoDetalleDtoResponse;
import com.example.LogiTrack.service.MovimientoDetalleService;
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

@Tag(name = "Gestión de detalles de movimiento", description = "API para registrar los productos y cantidades de cada movimiento")
@RestController
@RequestMapping("/api/movimientodetalle")
@RequiredArgsConstructor
public class MovimientoDetalleController {

    private final MovimientoDetalleService movimientoDetalleService;

    @Operation(summary = "Registrar detalle de movimiento", description = "Agrega un producto con su cantidad a un movimiento existente y actualiza el stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Stock insuficiente o datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Movimiento o producto no encontrado")
    })
    @PostMapping
    public ResponseEntity<MovimientoDetalleDtoResponse> guardar(@Valid @RequestBody MovimientoDetalleDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoDetalleService.guardar(dto));
    }

    @Operation(summary = "Listar todos los detalles", description = "Retorna todos los detalles de movimiento registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<MovimientoDetalleDtoResponse>> listar() {
        return ResponseEntity.ok(movimientoDetalleService.listar());
    }

    @Operation(summary = "Buscar detalle por ID", description = "Retorna un detalle de movimiento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDetalleDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoDetalleService.buscarId(id));
    }

    @Operation(summary = "Buscar detalles por movimiento", description = "Retorna todos los productos de un movimiento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/movimiento/{movimientoId}")
    public ResponseEntity<List<MovimientoDetalleDtoResponse>> buscarPorMovimiento(@PathVariable Long movimientoId) {
        return ResponseEntity.ok(movimientoDetalleService.buscarPorMovimiento(movimientoId));
    }

    @Operation(summary = "Buscar detalles por producto", description = "Retorna todos los movimientos en los que aparece un producto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<MovimientoDetalleDtoResponse>> buscarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(movimientoDetalleService.buscarPorProducto(productoId));
    }

    @Operation(summary = "Productos más movidos", description = "Reporte de los productos con mayor cantidad total movida — requerido en la rúbrica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente")
    })
    @GetMapping("/reporte/productosMasMovidos")
    public ResponseEntity<List<Map<String, Object>>> productosMasMovidos() {
        return ResponseEntity.ok(movimientoDetalleService.productosMasMovidos());
    }

    @Operation(summary = "Eliminar detalle de movimiento", description = "Elimina un detalle de movimiento por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Detalle eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        movimientoDetalleService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
