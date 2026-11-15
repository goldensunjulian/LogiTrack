package com.example.LogiTrack.mapper;

import com.example.LogiTrack.dto.request.BodegaDtoRequest;
import com.example.LogiTrack.dto.response.BodegaDtoResponse;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.modelo.Bodega;
import com.example.LogiTrack.modelo.Usuario;
import org.springframework.stereotype.Component;

@Component
public class BodegaMapper {

    public BodegaDtoResponse entityToDto(Bodega bodega, UsuarioDtoResponse encargadoDto) {
        if (bodega == null) return null;
        return new BodegaDtoResponse(
                bodega.getId(),
                bodega.getNombre(),
                bodega.getCiudad(),
                bodega.getDireccion(),
                bodega.getCapacidad(),
                // encargadoDto puede ser null si no hay encargado asignado
                encargadoDto,
                bodega.getActivado()
        );
    }

    public Bodega dtoToEntity(BodegaDtoRequest dto, Usuario encargado) {
        Bodega bodega = new Bodega();
        bodega.setNombre(dto.nombre());
        bodega.setCiudad(dto.ciudad());
        bodega.setDireccion(dto.direccion());
        bodega.setCapacidad(dto.capacidad());
        bodega.setUsuarioEncargado(encargado);
        bodega.setActivado(true);
        return bodega;
    }

    public void updateEntityFromDto(Bodega bodega, BodegaDtoRequest dto, Usuario encargado) {
        if (dto == null || bodega == null) return;
        if (dto.nombre() != null) bodega.setNombre(dto.nombre());
        if (dto.ciudad() != null) bodega.setCiudad(dto.ciudad());
        if (dto.direccion() != null) bodega.setDireccion(dto.direccion());
        if (dto.capacidad() != null) bodega.setCapacidad(dto.capacidad());
        bodega.setUsuarioEncargado(encargado);
    }
}
