package com.example.LogiTrack.controller;

import com.example.LogiTrack.dto.request.CategoriaDtoRequest;
import com.example.LogiTrack.dto.response.CategoriaDtoResponse;
import com.example.LogiTrack.service.CategoriaService;
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

@Tag(name = "Gestión de categorías", description = "API para registrar, consultar, actualizar y eliminar categorías de productos")
@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Registrar una categoría", description = "Crea una nueva categoría de productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Nombre de categoría ya existente")
    })
    @PostMapping
    public ResponseEntity<CategoriaDtoResponse> guardar(@Valid @RequestBody CategoriaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.guardar(dto));
    }

    @Operation(summary = "Listar todas las categorías", description = "Retorna todas las categorías registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaDtoResponse>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @Operation(summary = "Buscar categoría por ID", description = "Retorna una categoría específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarId(id));
    }

    @Operation(summary = "Actualizar categoría", description = "Modifica los datos de una categoría existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDtoResponse> actualizar(@Valid @RequestBody CategoriaDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.actualizar(dto, id));
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría si no tiene productos asociados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "La categoría tiene productos asociados"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
