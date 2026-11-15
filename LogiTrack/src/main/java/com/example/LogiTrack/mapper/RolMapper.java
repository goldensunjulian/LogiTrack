package com.example.LogiTrack.mapper;

import com.example.LogiTrack.dto.request.RolDtoRequest;
import com.example.LogiTrack.dto.response.RolDtoResponse;
import com.example.LogiTrack.modelo.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public RolDtoResponse entityToDto(Rol rol) {
        if (rol == null) return null;
        return new RolDtoResponse(
                rol.getId(),
                rol.getNombre()
        );
    }

    public Rol dtoToEntity(RolDtoRequest dto) {
        Rol rol = new Rol();
        rol.setNombre(dto.nombre());
        return rol;
    }

    public void updateEntityFromDto(Rol rol, RolDtoRequest dto) {
        if (dto == null || rol == null) return;
        if (dto.nombre() != null) rol.setNombre(dto.nombre());
    }
}
