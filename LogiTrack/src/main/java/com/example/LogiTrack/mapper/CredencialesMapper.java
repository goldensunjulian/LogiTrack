package com.example.LogiTrack.mapper;

import com.example.LogiTrack.dto.request.CredencialesDtoRequest;
import com.example.LogiTrack.dto.response.CredencialesDtoResponse;
import com.example.LogiTrack.dto.response.RolDtoResponse;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.modelo.Credenciales;
import com.example.LogiTrack.modelo.Rol;
import com.example.LogiTrack.modelo.Usuario;
import org.springframework.stereotype.Component;

@Component
public class CredencialesMapper {

    public CredencialesDtoResponse entityToDto(Credenciales credenciales, UsuarioDtoResponse usuarioDto, RolDtoResponse rolDto) {
        if (credenciales == null || usuarioDto == null || rolDto == null) return null;
        return new CredencialesDtoResponse(
                credenciales.getId(),
                credenciales.getUsername(),
                usuarioDto,
                rolDto
        );
    }

    // El service busca Usuario y Rol en BD y los pasa aquí
    // El password llega en texto plano, el service lo hashea antes de llamar este método
    public Credenciales dtoToEntity(CredencialesDtoRequest dto, Usuario usuario, Rol rol) {
        Credenciales credenciales = new Credenciales();
        credenciales.setUsername(dto.username());
        credenciales.setPassword(dto.password());
        credenciales.setUsuario(usuario);
        credenciales.setRol(rol);
        return credenciales;
    }

    public void updateEntityFromDto(Credenciales credenciales, CredencialesDtoRequest dto, Rol rol) {
        if (dto == null || credenciales == null) return;
        if (dto.username() != null) credenciales.setUsername(dto.username());
        if (rol != null) credenciales.setRol(rol);
        // El usuario no se cambia en un update de credenciales
    }
}
