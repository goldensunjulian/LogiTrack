package com.example.LogiTrack.controller;

import com.example.LogiTrack.dto.request.ProductoDtoRequest;
import com.example.LogiTrack.dto.response.ProductoDtoResponse;
import com.example.LogiTrack.service.ProductoService;
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

@Tag(name = "Gestión de productos", description = "API para registrar, consultar, actualizar y desactivar productos")
@RestController
@RequestMapping("/api/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Registrar un producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o producto ya existente")
    })
    @PostMapping
    public ResponseEntity<ProductoDtoResponse> guardar(@Valid @RequestBody ProductoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardar(dto));
    }

    @Operation(summary = "Listar todos los productos", description = "Retorna todos los productos activos e inactivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ProductoDtoResponse>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @Operation(summary = "Listar productos activos", description = "Retorna solo los productos con estado activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/activos")
    public ResponseEntity<List<ProductoDtoResponse>> listarActivos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @Operation(summary = "Buscar producto por ID", description = "Retorna un producto específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarId(id));
    }

    @Operation(summary = "Buscar productos por categoría", description = "Retorna productos que pertenezcan a una categoría específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoDtoResponse>> buscarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoriaId));
    }

    @Operation(summary = "Actualizar producto", description = "Modifica los datos de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDtoResponse> actualizar(@Valid @RequestBody ProductoDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(productoService.actualizar(dto, id));
    }

    @Operation(summary = "Desactivar producto", description = "Marca el producto como inactivo sin borrarlo de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Activar producto", description = "Reactiva un producto que estaba inactivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto activado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        productoService.activar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
