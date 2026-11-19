package com.example.LogiTrack.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration // Le digo a Spring que esta clase es de configuración de seguridad
@RequiredArgsConstructor
public class SecurityConfig {

    // Inyecto mi filtro JWT personalizado
    // Este filtro es el que va a interceptar y leer el token en cada petición
    private final JwtFilter jwtFilter;

    @Bean // Es vital dejar el @Bean para que Spring ejecute este metodo al arrancar la app
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*
         * Aquí estoy configurando toda la seguridad de la aplicación.
         * En Spring Security moderno se define un SecurityFilterChain como Bean.
         */
        return http
                // Habilito CORS basándome en el metodo corsConfigurationSource() de abajo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Desactivo CSRF porque mi API es REST y trabaja con JWT (stateless).
                // CSRF se usa más cuando hay sesiones clásicas en el servidor y formularios.
                .csrf(csrf -> csrf.disable())

                // Le digo a Spring que no quiero guardar estado (sesiones).
                // Cada request debe venir autenticado con su propio token nuevo.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Aquí defino qué endpoints son públicos y cuáles requieren ciertos roles.
                .authorizeHttpRequests(auth -> auth
                        // Solo el login es público (no /auth/**)
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/crearEmpleado").hasRole("SUPERADMIN")

                        // Permitir OPTIONS para CORS y Swagger público
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                        // Lectura abierta para los 3 roles
                        .requestMatchers(HttpMethod.GET,
                                "/api/bodega/**",
                                "/api/categoria/**",
                                "/api/producto/**").hasAnyRole("SUPERADMIN", "ADMIN", "EMPLEADO")

                        // Permitir al ADMIN listar usuarios activos para asignar encargados de bodega
                        .requestMatchers(HttpMethod.GET, "/api/usuario/activos").hasAnyRole("SUPERADMIN", "ADMIN")

                        // CRUD base solo para SUPERADMIN y ADMIN
                        .requestMatchers("/api/bodega/**",
                                "/api/categoria/**",
                                "/api/producto/**").hasAnyRole("SUPERADMIN", "ADMIN")

                        // Operaciones diarias para los 3 roles
                        .requestMatchers("/api/inventario/**",
                                "/api/movimiento/**",
                                "/api/movimientodetalle/**").hasAnyRole("SUPERADMIN", "ADMIN", "EMPLEADO")

                        // Exclusivos del SUPERADMIN
                        .requestMatchers("/api/usuario/**").hasRole("SUPERADMIN")
                        .requestMatchers("/api/credenciales/**").hasRole("SUPERADMIN")
                        .requestMatchers("/api/auditoria/**").hasRole("SUPERADMIN")

                        .anyRequest().authenticated()
                )

                /*
                 * Aquí agrego mi filtro JWT ANTES del filtro predeterminado UsernamePasswordAuthenticationFilter.
                 * Lo hago porque quiero que primero se valide el token JWT
                 * antes de que Spring intente buscar usuario/contraseña tradicionales.
                 */
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // por ultimo  construyo la configuración y se la entrego a Spring
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Usamos OriginPatterns con "*" para permitir que cualquier PC de la red local
        // (por ejemplo tu PC corriendo el frontend) se conecte al backend en el PC del profe sin errores de CORS.
        config.setAllowedOriginPatterns(List.of("*"));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Permitir todos los headers (incluyendo Authorization para enviar el token)
        config.setAllowedHeaders(List.of("*"));

        // Permite el envío de credenciales/tokens de manera cruzada
        config.setAllowCredentials(true);

        // Aplico esta regla de CORS a absolutamente todas las rutas de mi API ("/**")
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}