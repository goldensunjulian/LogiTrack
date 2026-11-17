package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.MovimientoDetalleDtoRequest;
import com.example.LogiTrack.dto.response.MovimientoDetalleDtoResponse;
import com.example.LogiTrack.dto.response.MovimientoDtoResponse;
import com.example.LogiTrack.dto.response.ProductoDtoResponse;
import com.example.LogiTrack.exception.BussinesRuleException;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.*;
import com.example.LogiTrack.modelo.*;
import com.example.LogiTrack.repository.*;
import com.example.LogiTrack.service.AuditoriaService;
import com.example.LogiTrack.service.MovimientoDetalleService;
import com.example.LogiTrack.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovimientoDetalleServiceImpl implements MovimientoDetalleService {

    private final MovimientoDetalleRepository detalleRepository;
    private final MovimientoDetalleMapper detalleMapper;
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaMapper categoriaMapper;
    private final InventarioRepository inventarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final BodegaMapper bodegaMapper;
    private final AuditoriaService auditoriaService;
    private final JsonUtil jsonUtil;

    private ProductoDtoResponse toProductoDto(Producto p) {
        return productoMapper.entityToDto(p, categoriaMapper.entityToDto(p.getCategoria()));
    }

    private MovimientoDtoResponse toMovimientoDto(Movimiento m) {
        return movimientoMapper.entityToDto(m,
                usuarioMapper.entityToDto(m.getUsuario()),
                m.getBodegaOrigen() != null ? bodegaMapper.entityToDto(m.getBodegaOrigen(),
                        m.getBodegaOrigen().getUsuarioEncargado() != null ?
                                usuarioMapper.entityToDto(m.getBodegaOrigen().getUsuarioEncargado()) : null) : null,
                m.getBodegaDestino() != null ? bodegaMapper.entityToDto(m.getBodegaDestino(),
                        m.getBodegaDestino().getUsuarioEncargado() != null ?
                                usuarioMapper.entityToDto(m.getBodegaDestino().getUsuarioEncargado()) : null) : null);
    }

    @Override
    @Transactional
    public MovimientoDetalleDtoResponse guardar(MovimientoDetalleDtoRequest dto) {
        Movimiento movimiento = movimientoRepository.findById(dto.movimientoId())
                .orElseThrow(() -> new DatabaseRuleException("Movimiento no encontrado"));
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new DatabaseRuleException("Producto no encontrado"));

        if (movimiento.getTipoMovimiento() == TipoMovimiento.entrada) {
            Inventario inv = inventarioRepository.findByBodegaIdAndProductoId(
                    movimiento.getBodegaDestino().getId(), producto.getId());
            if (inv == null) throw new DatabaseRuleException("No existe inventario para ese producto en la bodega destino");
            inv.setStock(inv.getStock() + dto.cantidad());
            inventarioRepository.save(inv);
        } else if (movimiento.getTipoMovimiento() == TipoMovimiento.salida) {
            Inventario inv = inventarioRepository.findByBodegaIdAndProductoId(
                    movimiento.getBodegaOrigen().getId(), producto.getId());
            if (inv == null) throw new DatabaseRuleException("No existe inventario para ese producto en la bodega origen");
            if (inv.getStock() < dto.cantidad())
                throw new BussinesRuleException("Stock insuficiente para realizar la salida");
            inv.setStock(inv.getStock() - dto.cantidad());
            inventarioRepository.save(inv);
        } else if (movimiento.getTipoMovimiento() == TipoMovimiento.transferencia) {
            Inventario invOrigen = inventarioRepository.findByBodegaIdAndProductoId(
                    movimiento.getBodegaOrigen().getId(), producto.getId());
            if (invOrigen == null) throw new DatabaseRuleException("No existe inventario para ese producto en la bodega origen");
            if (invOrigen.getStock() < dto.cantidad())
                throw new BussinesRuleException("Stock insuficiente para realizar la transferencia");
            Inventario invDestino = inventarioRepository.findByBodegaIdAndProductoId(
                    movimiento.getBodegaDestino().getId(), producto.getId());
            if (invDestino == null) throw new DatabaseRuleException("No existe inventario para ese producto en la bodega destino");
            invOrigen.setStock(invOrigen.getStock() - dto.cantidad());
            invDestino.setStock(invDestino.getStock() + dto.cantidad());
            inventarioRepository.save(invOrigen);
            inventarioRepository.save(invDestino);
        }

        MovimientoDetalle detalle = detalleMapper.dtoToEntity(dto, movimiento, producto);
        detalleRepository.save(detalle);
        auditoriaService.registrar(TipoOperacion.insert, "movimiento_detalle", detalle.getId(), null, null, jsonUtil.toJson(detalle));
        return detalleMapper.entityToDto(detalle, toMovimientoDto(movimiento), toProductoDto(producto));
    }

    @Override
    public void eliminar(Long id) {
        MovimientoDetalle detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Detalle de movimiento no encontrado"));
        auditoriaService.registrar(TipoOperacion.delete, "movimiento_detalle", detalle.getId(), null, jsonUtil.toJson(detalle), null);
        detalleRepository.delete(detalle);
    }

    @Override
    public List<MovimientoDetalleDtoResponse> listar() {
        return detalleRepository.findAll().stream().map(d ->
                detalleMapper.entityToDto(d, toMovimientoDto(d.getMovimiento()), toProductoDto(d.getProducto()))
        ).toList();
    }

    @Override
    public MovimientoDetalleDtoResponse buscarId(Long id) {
        MovimientoDetalle detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Detalle de movimiento no encontrado"));
        return detalleMapper.entityToDto(detalle, toMovimientoDto(detalle.getMovimiento()), toProductoDto(detalle.getProducto()));
    }

    @Override
    public List<MovimientoDetalleDtoResponse> buscarPorMovimiento(Long movimientoId) {
        return detalleRepository.findByMovimientoId(movimientoId).stream().map(d ->
                detalleMapper.entityToDto(d, toMovimientoDto(d.getMovimiento()), toProductoDto(d.getProducto()))
        ).toList();
    }

    @Override
    public List<MovimientoDetalleDtoResponse> buscarPorProducto(Long productoId) {
        return detalleRepository.findByProductoId(productoId).stream().map(d ->
                detalleMapper.entityToDto(d, toMovimientoDto(d.getMovimiento()), toProductoDto(d.getProducto()))
        ).toList();
    }

    @Override
    public List<Map<String, Object>> productosMasMovidos() {
        return detalleRepository.productosMasMovidos().stream().map(row -> {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("producto", row[0]);
            mapa.put("totalMovido", row[1]);
            return mapa;
        }).toList();
    }
}
