package com.example.LogiTrack.auth;

public record LoginResponse(
        String token,
        String username,
        String rol,
        Long usuarioId
) {
}
