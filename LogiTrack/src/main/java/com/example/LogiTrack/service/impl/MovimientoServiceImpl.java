package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.MovimientoDtoRequest;
import com.example.LogiTrack.dto.response.BodegaDtoResponse;
import com.example.LogiTrack.dto.response.MovimientoDtoResponse;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.exception.BussinesRuleException;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.BodegaMapper;
import com.example.LogiTrack.mapper.MovimientoMapper;
import com.example.LogiTrack.mapper.UsuarioMapper;
import com.example.LogiTrack.modelo.*;
import com.example.LogiTrack.repository.*;
import com.example.LogiTrack.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final BodegaRepository bodegaRepository;
    private final BodegaMapper bodegaMapper;

    private BodegaDtoResponse toBodegaDto(Bodega b) {
        return b != null ? bodegaMapper.entityToDto(b,
                b.getUsuarioEncargado() != null ? usuarioMapper.entityToDto(b.getUsuarioEncargado()) : null) : null;
    }

    private MovimientoDtoResponse toDto(Movimiento m) {
        return movimientoMapper.entityToDto(m,
                usuarioMapper.entityToDto(m.getUsuario()),
                toBodegaDto(m.getBodegaOrigen()),
                toBodegaDto(m.getBodegaDestino()));
    }

    @Override
    public MovimientoDtoResponse guardar(MovimientoDtoRequest dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("Usuario no encontrado"));

        Bodega bodegaOrigen = null;
        Bodega bodegaDestino = null;

        // Validar bodegas según el tipo de movimiento
        if (dto.tipoMovimiento() == TipoMovimiento.entrada) {
            if (dto.bodegaDestinoId() == null)
                throw new BussinesRuleException("Una entrada requiere bodega destino");
            bodegaDestino = bodegaRepository.findById(dto.bodegaDestinoId())
                    .orElseThrow(() -> new DatabaseRuleException("Bodega destino no encontrada"));
        } else if (dto.tipoMovimiento() == TipoMovimiento.salida) {
            if (dto.bodegaOrigenId() == null)
                throw new BussinesRuleException("Una salida requiere bodega origen");
            bodegaOrigen = bodegaRepository.findById(dto.bodegaOrigenId())
                    .orElseThrow(() -> new DatabaseRuleException("Bodega origen no encontrada"));
        } else if (dto.tipoMovimiento() == TipoMovimiento.transferencia) {
            if (dto.bodegaOrigenId() == null || dto.bodegaDestinoId() == null)
                throw new BussinesRuleException("Una transferencia requiere bodega origen y destino");
            if (dto.bodegaOrigenId().equals(dto.bodegaDestinoId()))
                throw new BussinesRuleException("La bodega origen y destino no pueden ser la misma");
            bodegaOrigen = bodegaRepository.findById(dto.bodegaOrigenId())
                    .orElseThrow(() -> new DatabaseRuleException("Bodega origen no encontrada"));
            bodegaDestino = bodegaRepository.findById(dto.bodegaDestinoId())
                    .orElseThrow(() -> new DatabaseRuleException("Bodega destino no encontrada"));
        }

        // Validar que el usuario sea el encargado de la bodega origen o destino
        Bodega bodegaDelUsuario = bodegaRepository.findByUsuarioEncargadoId(usuario.getId());
        if (bodegaDelUsuario == null)
            throw new BussinesRuleException("El usuario no es encargado de ninguna bodega");

        Movimiento movimiento = movimientoMapper.dtoToEntity(dto, usuario, bodegaOrigen, bodegaDestino);
        return toDto(movimientoRepository.save(movimiento));
    }

    @Override
    public void eliminar(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Movimiento no encontrado"));
        movimientoRepository.delete(movimiento);
    }

    @Override
    public List<MovimientoDtoResponse> listar() {
        return movimientoRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public MovimientoDtoResponse buscarId(Long id) {
        return toDto(movimientoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Movimiento no encontrado")));
    }

    @Override
    public List<MovimientoDtoResponse> buscarPorUsuario(Long usuarioId) {
        return movimientoRepository.findByUsuarioId(usuarioId).stream().map(this::toDto).toList();
    }

    @Override
    public List<MovimientoDtoResponse> buscarPorTipo(String tipoMovimiento) {
        TipoMovimiento tipo = TipoMovimiento.valueOf(tipoMovimiento.toLowerCase());
        return movimientoRepository.findByTipoMovimiento(tipo).stream().map(this::toDto).toList();
    }

    @Override
    public List<MovimientoDtoResponse> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.findByFechaBetween(inicio, fin).stream().map(this::toDto).toList();
    }
}
