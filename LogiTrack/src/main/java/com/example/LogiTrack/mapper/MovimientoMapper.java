package com.example.LogiTrack.mapper;

import com.example.LogiTrack.dto.request.MovimientoDtoRequest;
import com.example.LogiTrack.dto.response.BodegaDtoResponse;
import com.example.LogiTrack.dto.response.MovimientoDtoResponse;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.modelo.Bodega;
import com.example.LogiTrack.modelo.Movimiento;
import com.example.LogiTrack.modelo.Usuario;
import org.springframework.stereotype.Component;

@Component
public class MovimientoMapper {

    // bodegaOrigenDto y bodegaDestinoDto pueden ser null según el tipo de movimiento
    public MovimientoDtoResponse entityToDto(Movimiento movimiento, UsuarioDtoResponse usuarioDto,
                                              BodegaDtoResponse bodegaOrigenDto, BodegaDtoResponse bodegaDestinoDto) {
        if (movimiento == null || usuarioDto == null) return null;
        return new MovimientoDtoResponse(
                movimiento.getId(),
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                usuarioDto,
                bodegaOrigenDto,
                bodegaDestinoDto
        );
    }

    // El service busca Usuario, BodegaOrigen y BodegaDestino en BD y los pasa aquí
    public Movimiento dtoToEntity(MovimientoDtoRequest dto, Usuario usuario, Bodega bodegaOrigen, Bodega bodegaDestino) {
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(dto.tipoMovimiento());
        movimiento.setUsuario(usuario);
        movimiento.setBodegaOrigen(bodegaOrigen);
        movimiento.setBodegaDestino(bodegaDestino);
        return movimiento;
    }

    // Los movimientos no se actualizan, son registros históricos inmutables
}
