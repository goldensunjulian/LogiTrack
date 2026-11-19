package com.example.LogiTrack.auth;
import com.example.LogiTrack.config.JwtService;
import com.example.LogiTrack.exception.BussinesRuleException;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.modelo.Credenciales;
import com.example.LogiTrack.modelo.Rol;
import com.example.LogiTrack.modelo.Usuario;
import com.example.LogiTrack.repository.CredencialesRepository;
import com.example.LogiTrack.repository.RolRepository;
import com.example.LogiTrack.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "Autenticación", description = "Endpoints para login y gestión de acceso")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final CredencialesRepository credencialesRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, token generado"),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Credenciales credenciales = credencialesRepository.findByUsername(request.username());
        if (credenciales == null)
            throw new BussinesRuleException("Credenciales inválidas");
        if (!credenciales.getPassword().equals(request.password()))
            throw new BussinesRuleException("Credenciales inválidas");
        if (!credenciales.getUsuario().isActivo())
            throw new BussinesRuleException("El usuario está inactivo");
        String rol = credenciales.getRol().getNombre();
        String token = jwtService.generateToken(credenciales.getUsername(), rol);
        return ResponseEntity.ok(new LoginResponse(
                token,
                credenciales.getUsername(),
                rol,
                credenciales.getUsuario().getId()
        ));    }

    @Operation(summary = "Crear empleado", description = "El ADMIN crea un nuevo empleado con usuario y credenciales en un solo paso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Email o username ya registrado"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @PostMapping("/crearEmpleado")
    public ResponseEntity<Map<String, String>> crearEmpleado(@Valid @RequestBody CrearEmpleadoRequest request) {
        if (usuarioRepository.existsByEmail(request.email()))
            throw new BussinesRuleException("El email ya está registrado");
        if (credencialesRepository.existsByUsername(request.username()))
            throw new BussinesRuleException("El username ya está tomado");
        Rol rol = rolRepository.findById(request.rolId()).orElseThrow(() -> new DatabaseRuleException("Rol no encontrado"));
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
        Credenciales credenciales = new Credenciales();
        credenciales.setUsername(request.username());
        credenciales.setPassword(request.password());
        credenciales.setUsuario(usuario);
        credenciales.setRol(rol);
        credencialesRepository.save(credenciales);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Empleado creado correctamente", "username", credenciales.getUsername()));
    }
}
