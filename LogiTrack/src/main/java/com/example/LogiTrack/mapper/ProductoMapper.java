package com.example.LogiTrack.mapper;

import com.example.LogiTrack.dto.request.ProductoDtoRequest;
import com.example.LogiTrack.dto.response.CategoriaDtoResponse;
import com.example.LogiTrack.dto.response.ProductoDtoResponse;
import com.example.LogiTrack.modelo.Categoria;
import com.example.LogiTrack.modelo.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDtoResponse entityToDto(Producto producto, CategoriaDtoResponse categoriaDto) {
        if (producto == null || categoriaDto == null) return null;
        return new ProductoDtoResponse(
                producto.getId(),
                producto.getNombre(),
                categoriaDto,
                producto.getPrecio(),
                producto.isActivo()
        );
    }

    public Producto dtoToEntity(ProductoDtoRequest dto, Categoria categoria) {
        Producto producto = new Producto();
        producto.setNombre(dto.nombre());
        producto.setCategoria(categoria);
        producto.setPrecio(dto.precio());
        producto.setActivo(dto.activo());
        return producto;
    }

    public void updateEntityFromDto(Producto producto, ProductoDtoRequest dto, Categoria categoria) {
        if (dto == null || producto == null) return;
        if (dto.nombre() != null) producto.setNombre(dto.nombre());
        if (categoria != null) producto.setCategoria(categoria);
        if (dto.precio() != null) producto.setPrecio(dto.precio());
        if (dto.activo() != null) producto.setActivo(dto.activo());
    }
}
