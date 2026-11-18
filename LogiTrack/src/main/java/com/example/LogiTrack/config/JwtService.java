package com.example.LogiTrack.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    //clave "secreta"
    private final String SECRET = "clave_super_secreta_logitrack_2026";
    //tiempo expiracion
    private final long EXPIRATION = 1000 * 60 * 30; // 30 minutos

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    //generar token xd con username y rol
    public String generateToken(String username, String rol) {
        return Jwts.builder()
                //usuario
                .setSubject(username)
                //rol, se usa /.claim porque es algo adicional, como un carnet
                .claim("rol", rol)
                //fecha creacion
                .setIssuedAt(new Date())
                //fecha expiracion
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                // Firma el token usando el algoritmo HS256 y nuestra clave secreta
                // (convertida a Key por el metodo getKey)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                //junta lo anterior para crear el token
                .compact();
    }
    // Validar token
    public String validateToken(String token) {
        /*
         * Este método:
         * - Verifica que la firma sea correcta
         * - Verifica que no esté expirado
         * - Si todo está bien, devuelve el username
         *
         * Si algo falla, devuelve null.
         */
        try {
            return Jwts.parserBuilder()
                    // Le paso la misma clave con la que firmé
                    .setSigningKey(getKey())
                    .build()

                    // Intenta parsear el token
                    .parseClaimsJws(token)

                    // Obtengo el body (claims)
                    .getBody()

                    // Devuelvo el subject (username)
                    .getSubject();
        } catch (Exception e) {
            /*
             * Si el token:
             * - Está vencido
             * - Fue modificado
             * - La firma no coincide
             * Lanza excepción y retorna en null.
             */
            return null;
        }
    }

    // Extraer el rol del token
    public String getRolFromToken(String token) {
        /*
         * Este metodo:
         * - Abre el token validando su firma con nuestra clave secreta.
         * - Accede al cuerpo del token (Payload/Claims).
         * - Busca específicamente el dato extra que inyectamos llamado "rol".
         *
         * Lo usamos en el JwtFilter para cargar el nivel de acceso en el SecurityContext.
         */
        try {
            return Jwts.parserBuilder()
                    // Le paso la misma clave con la que firmé
                    .setSigningKey(getKey())
                    .build()

                    // Intenta parsear el token
                    .parseClaimsJws(token)

                    // Obtengo el body (claims)
                    .getBody()

                    // Devuelvo el rol
                    .get("rol", String.class);

        } catch (Exception e) {
            /*
             * Si el token:
             * - Está vencido
             * - Fue modificado por un usuario malintencionado
             * - No tiene la firma correcta
             * Lanza excepción, la capturamos y retornamos null para denegar el rol.
             */
            return null;
        }
    }
}
