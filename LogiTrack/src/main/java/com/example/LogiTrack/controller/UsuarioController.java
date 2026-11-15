package com.example.LogiTrack.controller;

import com.example.LogiTrack.dto.request.UsuarioDtoRequest;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.service.UsuarioService;
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

@Tag(name = "Gestión de usuarios", description = "API para registrar, consultar, actualizar y desactivar usuarios del sistema")
@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o email ya registrado")
    })
    @PostMapping
    public ResponseEntity<UsuarioDtoResponse> guardar(@Valid @RequestBody UsuarioDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(dto));
    }

    @Operation(summary = "Listar todos los usuarios", description = "Retorna todos los usuarios activos e inactivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioDtoResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @Operation(summary = "Listar usuarios activos", description = "Retorna solo los usuarios con estado activo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioDtoResponse>> listarActivos() {
        return ResponseEntity.ok(usuarioService.listarActivos());
    }

    @Operation(summary = "Buscar usuario por ID", description = "Retorna un usuario específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarId(id));
    }

    @Operation(summary = "Buscar usuarios por rol", description = "Retorna usuarios que tengan el rol indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/rol")
    public ResponseEntity<List<UsuarioDtoResponse>> buscarPorRol(@RequestParam String nombreRol) {
        return ResponseEntity.ok(usuarioService.buscarPorRol(nombreRol));
    }

    @Operation(summary = "Actualizar usuario", description = "Modifica los datos de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDtoResponse> actualizar(@Valid @RequestBody UsuarioDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.actualizar(dto, id));
    }

    @Operation(summary = "Desactivar usuario", description = "Marca el usuario como inactivo sin borrarlo de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
