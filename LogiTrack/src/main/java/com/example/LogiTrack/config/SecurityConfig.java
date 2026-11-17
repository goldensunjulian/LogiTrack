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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas — no requieren token
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Swagger — público para poder documentar y probar
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // Rutas solo para SUPERADMIN
                        .requestMatchers("/api/usuario/**").hasRole("SUPERADMIN")
                        .requestMatchers("/api/credenciales/**").hasRole("SUPERADMIN")
                        .requestMatchers("/auth/crearEmpleado").hasAnyRole("SUPERADMIN", "ADMIN")
                        // Rutas para ADMIN
                        .requestMatchers("/api/bodega/**").hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers("/api/categoria/**").hasAnyRole("SUPERADMIN", "ADMIN")
                        .requestMatchers("/api/auditoria/**").hasAnyRole("SUPERADMIN", "ADMIN")
                        // Rutas para ADMIN y EMPLEADO
                        .requestMatchers("/api/producto/**").hasAnyRole("SUPERADMIN", "ADMIN", "EMPLEADO")
                        .requestMatchers("/api/inventario/**").hasAnyRole("SUPERADMIN", "ADMIN", "EMPLEADO")
                        .requestMatchers("/api/movimiento/**").hasAnyRole("SUPERADMIN", "ADMIN", "EMPLEADO")
                        .requestMatchers("/api/movimientodetalle/**").hasAnyRole("SUPERADMIN", "ADMIN", "EMPLEADO")
                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
