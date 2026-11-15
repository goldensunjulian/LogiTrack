package com.example.LogiTrack.mapper;

import com.example.LogiTrack.dto.request.MovimientoDetalleDtoRequest;
import com.example.LogiTrack.dto.response.MovimientoDetalleDtoResponse;
import com.example.LogiTrack.dto.response.MovimientoDtoResponse;
import com.example.LogiTrack.dto.response.ProductoDtoResponse;
import com.example.LogiTrack.modelo.Movimiento;
import com.example.LogiTrack.modelo.MovimientoDetalle;
import com.example.LogiTrack.modelo.Producto;
import org.springframework.stereotype.Component;

@Component
public class MovimientoDetalleMapper {

    public MovimientoDetalleDtoResponse entityToDto(MovimientoDetalle detalle, MovimientoDtoResponse movimientoDto, ProductoDtoResponse productoDto) {
        if (detalle == null || movimientoDto == null || productoDto == null) return null;
        return new MovimientoDetalleDtoResponse(
                detalle.getId(),
                movimientoDto,
                productoDto,
                detalle.getCantidad()
        );
    }

    // El service busca Movimiento y Producto en BD y los pasa aquí
    public MovimientoDetalle dtoToEntity(MovimientoDetalleDtoRequest dto, Movimiento movimiento, Producto producto) {
        MovimientoDetalle detalle = new MovimientoDetalle();
        detalle.setMovimiento(movimiento);
        detalle.setProducto(producto);
        detalle.setCantidad(dto.cantidad());
        return detalle;
    }

    // Los detalles de movimiento tampoco se actualizan, son históricos
}
