package com.example.LogiTrack.mapper;

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
}
