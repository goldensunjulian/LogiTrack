package com.example.LogiTrack.mapper;

import com.example.LogiTrack.dto.request.UsuarioDtoRequest;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.modelo.Usuario;
import org.springframework.stereotype.Component;

@Component

public class UsuarioMapper {
    public UsuarioDtoResponse entityToDto(Usuario usuario){
        if (usuario==null) return null;
        return new UsuarioDtoResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.isActivo()
        );
    }

    public Usuario dtoToEntity (UsuarioDtoRequest dto){
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setEmail(dto.email());
        return usuario;
    }

    public void updateEntityFromDto(Usuario usuario, UsuarioDtoRequest dto){
        if (dto==null || usuario==null) return;
        if (dto.nombre() != null) usuario.setNombre(dto.nombre());
        if (dto.email() != null) usuario.setEmail(dto.email());
        if (dto.activo() != null) usuario.setActivo(dto.activo());
    }
}
