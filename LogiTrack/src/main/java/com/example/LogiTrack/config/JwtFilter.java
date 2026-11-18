package com.example.LogiTrack.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

// Le digo a Spring que este filtro es un componente manejado por el contenedor
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilter {
    // inyectamos el jwtService
    private final JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        // Convierto el request genérico en HttpServletRequest
        HttpServletRequest http = (HttpServletRequest) request;

        // Obtengo el header Authorization
        String header = http.getHeader("Authorization");
        /*
         * Verifico:
         * 1) Que el header exista
         * 2) Que empiece con "Bearer "
         */
        //Bearer Token
        if (header != null && header.startsWith("Bearer ")) {

            // Quito la palabra "Bearer " y me quedo solo con el token (los primeros 7 caracteres)
            String token = header.substring(7);

            // Valido el token usando mi servicio
            String username = jwtService.validateToken(token);
            /*
             * Si el username no es null significa que:
             * - La firma es válida
             * - El token no está vencido
             */
            if (username != null) {
                /*
                 * Extraemos el rol del token (el claim personalizado que guardamos).
                 * Spring Security requiere por convención que los roles tengan el prefijo "ROLE_"
                 * para que funcionen las validaciones de permisos más adelante.
                 */
                String rol = jwtService.getRolFromToken(token);
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase());
                /*
                 * Creo un objeto de autenticación.
                 * Aquí estoy diciendo: "Este usuario ya está autenticado"
                 * No paso contraseña porque en JWT ya no la necesito.
                 * Y en vez de una lista vacía, le paso el rol que acabamos de extraer.
                 */
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(authority) // Aquí pasamos el rol en lugar de Collections.emptyList()
                        );
                /*
                 * Aquí es donde realmente le digo a Spring Security:
                 * "Este usuario está autenticado en esta petición y tiene este ROL"
                 */
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        /*
         * Muy importante:
         * Siempre debo continuar la cadena de filtros.
         * Si no llamo a chain.doFilter, la petición se queda bloqueada.
         */
        chain.doFilter(request, response);
    }
}
