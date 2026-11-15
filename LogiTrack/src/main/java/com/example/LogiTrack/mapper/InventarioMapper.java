package com.example.LogiTrack.mapper;

import com.example.LogiTrack.dto.request.InventarioDtoRequest;
import com.example.LogiTrack.dto.response.BodegaDtoResponse;
import com.example.LogiTrack.dto.response.InventarioDtoResponse;
import com.example.LogiTrack.dto.response.ProductoDtoResponse;
import com.example.LogiTrack.modelo.Bodega;
import com.example.LogiTrack.modelo.Inventario;
import com.example.LogiTrack.modelo.Producto;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

    public InventarioDtoResponse entityToDto(Inventario inventario, BodegaDtoResponse bodegaDto, ProductoDtoResponse productoDto) {
        if (inventario == null || bodegaDto == null || productoDto == null) return null;
        return new InventarioDtoResponse(
                inventario.getId(),
                bodegaDto,
                productoDto,
                inventario.getStock()
        );
    }

    // El service busca Bodega y Producto en BD y los pasa aquí
    public Inventario dtoToEntity(InventarioDtoRequest dto, Bodega bodega, Producto producto) {
        Inventario inventario = new Inventario();
        inventario.setBodega(bodega);
        inventario.setProducto(producto);
        inventario.setStock(dto.stock());
        return inventario;
    }

    public void updateEntityFromDto(Inventario inventario, InventarioDtoRequest dto) {
        if (dto == null || inventario == null) return;
        // Solo el stock es actualizable, bodega y producto no cambian en un inventario existente
        if (dto.stock() != null) inventario.setStock(dto.stock());
    }
}
