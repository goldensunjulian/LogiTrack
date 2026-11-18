package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.MovimientoDetalleDtoRequest;
import com.example.LogiTrack.dto.response.MovimientoDetalleDtoResponse;
import com.example.LogiTrack.exception.BussinesRuleException;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.*;
import com.example.LogiTrack.modelo.*;
import com.example.LogiTrack.repository.*;
import com.example.LogiTrack.service.MovimientoDetalleService;
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

    @Override
    @Transactional
    public MovimientoDetalleDtoResponse guardar(MovimientoDetalleDtoRequest dto) {
        Movimiento movimiento = movimientoRepository.findById(dto.movimientoId()).orElseThrow(() -> new DatabaseRuleException("Movimiento no encontrado"));
        Producto producto = productoRepository.findById(dto.productoId()).orElseThrow(() -> new DatabaseRuleException("Producto no encontrado"));

        if (movimiento.getTipoMovimiento() == TipoMovimiento.entrada) {
            Inventario inv = inventarioRepository.findByBodegaIdAndProductoId(
                    movimiento.getBodegaDestino().getId(), producto.getId());
            if (inv == null) {
                // No existe — se crea automáticamente con la cantidad inicial
                inv = new Inventario();
                inv.setBodega(movimiento.getBodegaDestino());
                inv.setProducto(producto);
                inv.setStock(dto.cantidad());
            } else {
                // Ya existe — se suma al stock actual
                inv.setStock(inv.getStock() + dto.cantidad());
            }
            inventarioRepository.save(inv);

        } else if (movimiento.getTipoMovimiento() == TipoMovimiento.salida) {
            Inventario inv = inventarioRepository.findByBodegaIdAndProductoId(
                    movimiento.getBodegaOrigen().getId(), producto.getId());
            if (inv == null)
                throw new DatabaseRuleException("No existe inventario para ese producto en la bodega origen");
            if (inv.getStock() < dto.cantidad())
                throw new BussinesRuleException("Stock insuficiente para realizar la salida");
            inv.setStock(inv.getStock() - dto.cantidad());
            inventarioRepository.save(inv);

        } else if (movimiento.getTipoMovimiento() == TipoMovimiento.transferencia) {
            Inventario invOrigen = inventarioRepository.findByBodegaIdAndProductoId(
                    movimiento.getBodegaOrigen().getId(), producto.getId());
            if (invOrigen == null)
                throw new DatabaseRuleException("No existe inventario para ese producto en la bodega origen");
            if (invOrigen.getStock() < dto.cantidad())
                throw new BussinesRuleException("Stock insuficiente para realizar la transferencia");

            Inventario invDestino = inventarioRepository.findByBodegaIdAndProductoId(
                    movimiento.getBodegaDestino().getId(), producto.getId());
            if (invDestino == null) {
                // No existe en destino — se crea automáticamente
                invDestino = new Inventario();
                invDestino.setBodega(movimiento.getBodegaDestino());
                invDestino.setProducto(producto);
                invDestino.setStock(dto.cantidad());
            } else {
                invDestino.setStock(invDestino.getStock() + dto.cantidad());
            }
            invOrigen.setStock(invOrigen.getStock() - dto.cantidad());
            inventarioRepository.save(invOrigen);
            inventarioRepository.save(invDestino);
        }

        MovimientoDetalle detalle = detalleMapper.dtoToEntity(dto, movimiento, producto);
        detalleRepository.save(detalle);

        // Convertir relaciones para el response
        var categoriaDto = categoriaMapper.entityToDto(producto.getCategoria());
        var productoDto = productoMapper.entityToDto(producto, categoriaDto);

        var usuarioDto = usuarioMapper.entityToDto(movimiento.getUsuario());

        var origenDto = movimiento.getBodegaOrigen() != null
                ? bodegaMapper.entityToDto(movimiento.getBodegaOrigen(),
                    movimiento.getBodegaOrigen().getUsuarioEncargado() != null
                        ? usuarioMapper.entityToDto(movimiento.getBodegaOrigen().getUsuarioEncargado())
                        : null)
                : null;

        var destinoDto = movimiento.getBodegaDestino() != null
                ? bodegaMapper.entityToDto(movimiento.getBodegaDestino(),
                    movimiento.getBodegaDestino().getUsuarioEncargado() != null
                        ? usuarioMapper.entityToDto(movimiento.getBodegaDestino().getUsuarioEncargado())
                        : null)
                : null;

        var movimientoDto = movimientoMapper.entityToDto(movimiento, usuarioDto, origenDto, destinoDto);

        return detalleMapper.entityToDto(detalle, movimientoDto, productoDto);
    }

    @Override
    public void eliminar(Long id) {
        MovimientoDetalle detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Detalle de movimiento no encontrado"));
        detalleRepository.delete(detalle);
    }

    @Override
    public List<MovimientoDetalleDtoResponse> listar() {
        return detalleRepository.findAll().stream().map(d -> {
            var categoriaDto = categoriaMapper.entityToDto(d.getProducto().getCategoria());
            var productoDto = productoMapper.entityToDto(d.getProducto(), categoriaDto);
            var usuarioDto = usuarioMapper.entityToDto(d.getMovimiento().getUsuario());
            var origenDto = d.getMovimiento().getBodegaOrigen() != null
                    ? bodegaMapper.entityToDto(d.getMovimiento().getBodegaOrigen(),
                        d.getMovimiento().getBodegaOrigen().getUsuarioEncargado() != null
                            ? usuarioMapper.entityToDto(d.getMovimiento().getBodegaOrigen().getUsuarioEncargado())
                            : null)
                    : null;
            var destinoDto = d.getMovimiento().getBodegaDestino() != null
                    ? bodegaMapper.entityToDto(d.getMovimiento().getBodegaDestino(),
                        d.getMovimiento().getBodegaDestino().getUsuarioEncargado() != null
                            ? usuarioMapper.entityToDto(d.getMovimiento().getBodegaDestino().getUsuarioEncargado())
                            : null)
                    : null;
            var movimientoDto = movimientoMapper.entityToDto(d.getMovimiento(), usuarioDto, origenDto, destinoDto);
            return detalleMapper.entityToDto(d, movimientoDto, productoDto);
        }).toList();
    }

    @Override
    public MovimientoDetalleDtoResponse buscarId(Long id) {
        MovimientoDetalle detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Detalle de movimiento no encontrado"));
        var categoriaDto = categoriaMapper.entityToDto(detalle.getProducto().getCategoria());
        var productoDto = productoMapper.entityToDto(detalle.getProducto(), categoriaDto);
        var usuarioDto = usuarioMapper.entityToDto(detalle.getMovimiento().getUsuario());
        var origenDto = detalle.getMovimiento().getBodegaOrigen() != null
                ? bodegaMapper.entityToDto(detalle.getMovimiento().getBodegaOrigen(),
                    detalle.getMovimiento().getBodegaOrigen().getUsuarioEncargado() != null
                        ? usuarioMapper.entityToDto(detalle.getMovimiento().getBodegaOrigen().getUsuarioEncargado())
                        : null)
                : null;
        var destinoDto = detalle.getMovimiento().getBodegaDestino() != null
                ? bodegaMapper.entityToDto(detalle.getMovimiento().getBodegaDestino(),
                    detalle.getMovimiento().getBodegaDestino().getUsuarioEncargado() != null
                        ? usuarioMapper.entityToDto(detalle.getMovimiento().getBodegaDestino().getUsuarioEncargado())
                        : null)
                : null;
        var movimientoDto = movimientoMapper.entityToDto(detalle.getMovimiento(), usuarioDto, origenDto, destinoDto);
        return detalleMapper.entityToDto(detalle, movimientoDto, productoDto);
    }

    @Override
    public List<MovimientoDetalleDtoResponse> buscarPorMovimiento(Long movimientoId) {
        return detalleRepository.findByMovimientoId(movimientoId).stream().map(d -> {
            var categoriaDto = categoriaMapper.entityToDto(d.getProducto().getCategoria());
            var productoDto = productoMapper.entityToDto(d.getProducto(), categoriaDto);
            var usuarioDto = usuarioMapper.entityToDto(d.getMovimiento().getUsuario());
            var origenDto = d.getMovimiento().getBodegaOrigen() != null
                    ? bodegaMapper.entityToDto(d.getMovimiento().getBodegaOrigen(),
                        d.getMovimiento().getBodegaOrigen().getUsuarioEncargado() != null
                            ? usuarioMapper.entityToDto(d.getMovimiento().getBodegaOrigen().getUsuarioEncargado())
                            : null)
                    : null;
            var destinoDto = d.getMovimiento().getBodegaDestino() != null
                    ? bodegaMapper.entityToDto(d.getMovimiento().getBodegaDestino(),
                        d.getMovimiento().getBodegaDestino().getUsuarioEncargado() != null
                            ? usuarioMapper.entityToDto(d.getMovimiento().getBodegaDestino().getUsuarioEncargado())
                            : null)
                    : null;
            var movimientoDto = movimientoMapper.entityToDto(d.getMovimiento(), usuarioDto, origenDto, destinoDto);
            return detalleMapper.entityToDto(d, movimientoDto, productoDto);
        }).toList();
    }

    @Override
    public List<MovimientoDetalleDtoResponse> buscarPorProducto(Long productoId) {
        return detalleRepository.findByProductoId(productoId).stream().map(d -> {
            var categoriaDto = categoriaMapper.entityToDto(d.getProducto().getCategoria());
            var productoDto = productoMapper.entityToDto(d.getProducto(), categoriaDto);
            var usuarioDto = usuarioMapper.entityToDto(d.getMovimiento().getUsuario());
            var origenDto = d.getMovimiento().getBodegaOrigen() != null
                    ? bodegaMapper.entityToDto(d.getMovimiento().getBodegaOrigen(),
                        d.getMovimiento().getBodegaOrigen().getUsuarioEncargado() != null
                            ? usuarioMapper.entityToDto(d.getMovimiento().getBodegaOrigen().getUsuarioEncargado())
                            : null)
                    : null;
            var destinoDto = d.getMovimiento().getBodegaDestino() != null
                    ? bodegaMapper.entityToDto(d.getMovimiento().getBodegaDestino(),
                        d.getMovimiento().getBodegaDestino().getUsuarioEncargado() != null
                            ? usuarioMapper.entityToDto(d.getMovimiento().getBodegaDestino().getUsuarioEncargado())
                            : null)
                    : null;
            var movimientoDto = movimientoMapper.entityToDto(d.getMovimiento(), usuarioDto, origenDto, destinoDto);
            return detalleMapper.entityToDto(d, movimientoDto, productoDto);
        }).toList();
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
