package com.example.LogiTrack.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "clave_super_secreta_logitrack_2026";
    private final long EXPIRATION = 1000 * 60 * 30; // 30 minutos

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Genera el token con el username y el rol adentro
    public String generateToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                // El rol va como claim adicional dentro del token
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Valida el token y devuelve el username, null si es inválido o expiró
    public String validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    // Extrae el rol del token — lo usa el JwtFilter para cargarlo en el SecurityContext
    public String getRolFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("rol", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}
