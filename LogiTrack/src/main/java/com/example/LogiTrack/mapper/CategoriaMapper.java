package com.example.LogiTrack.mapper;

import com.example.LogiTrack.dto.request.CategoriaDtoRequest;
import com.example.LogiTrack.dto.response.CategoriaDtoResponse;
import com.example.LogiTrack.modelo.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaDtoResponse entityToDto(Categoria categoria) {
        if (categoria == null) return null;
        return new CategoriaDtoResponse(
                categoria.getId(),
                categoria.getNombre()
        );
    }

    public Categoria dtoToEntity(CategoriaDtoRequest dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.nombre());
        return categoria;
    }

    public void updateEntityFromDto(Categoria categoria, CategoriaDtoRequest dto) {
        if (dto == null || categoria == null) return;
        if (dto.nombre() != null) categoria.setNombre(dto.nombre());
    }
}
