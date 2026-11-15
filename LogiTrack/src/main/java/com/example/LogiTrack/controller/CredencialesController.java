package com.example.LogiTrack.controller;

import com.example.LogiTrack.dto.request.CredencialesDtoRequest;
import com.example.LogiTrack.dto.response.CredencialesDtoResponse;
import com.example.LogiTrack.service.CredencialesService;
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

@Tag(name = "Gestión de credenciales", description = "API para crear, consultar, actualizar y eliminar credenciales de acceso")
@RestController
@RequestMapping("/api/credenciales")
@RequiredArgsConstructor
public class CredencialesController {

    private final CredencialesService credencialesService;

    @Operation(summary = "Registrar credenciales", description = "Crea credenciales de acceso para un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Credenciales registradas correctamente"),
            @ApiResponse(responseCode = "400", description = "Username ya tomado o datos inválidos")
    })
    @PostMapping
    public ResponseEntity<CredencialesDtoResponse> guardar(@Valid @RequestBody CredencialesDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(credencialesService.guardar(dto));
    }

    @Operation(summary = "Listar todas las credenciales", description = "Retorna todas las credenciales registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<CredencialesDtoResponse>> listar() {
        return ResponseEntity.ok(credencialesService.listar());
    }

    @Operation(summary = "Buscar credenciales por ID", description = "Retorna unas credenciales específicas por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credenciales encontradas correctamente"),
            @ApiResponse(responseCode = "404", description = "Credenciales no encontradas")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CredencialesDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(credencialesService.buscarId(id));
    }

    @Operation(summary = "Actualizar credenciales", description = "Modifica el username o rol de unas credenciales existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credenciales actualizadas correctamente"),
            @ApiResponse(responseCode = "404", description = "Credenciales no encontradas")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CredencialesDtoResponse> actualizar(@Valid @RequestBody CredencialesDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(credencialesService.actualizar(dto, id));
    }

    @Operation(summary = "Eliminar credenciales por ID", description = "Borra las credenciales de acceso por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Credenciales eliminadas correctamente"),
            @ApiResponse(responseCode = "404", description = "Credenciales no encontradas")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        credencialesService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Eliminar credenciales por usuario", description = "Borra las credenciales de un empleado cuando es despedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Credenciales eliminadas correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron credenciales para ese usuario")
    })
    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> eliminarPorUsuario(@PathVariable Long usuarioId) {
        credencialesService.eliminarPorUsuario(usuarioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
