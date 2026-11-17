package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.InventarioDtoRequest;
import com.example.LogiTrack.dto.response.BodegaDtoResponse;
import com.example.LogiTrack.dto.response.InventarioDtoResponse;
import com.example.LogiTrack.dto.response.ProductoDtoResponse;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.BodegaMapper;
import com.example.LogiTrack.mapper.CategoriaMapper;
import com.example.LogiTrack.mapper.InventarioMapper;
import com.example.LogiTrack.mapper.ProductoMapper;
import com.example.LogiTrack.mapper.UsuarioMapper;
import com.example.LogiTrack.modelo.Bodega;
import com.example.LogiTrack.modelo.Inventario;
import com.example.LogiTrack.modelo.Producto;
import com.example.LogiTrack.modelo.TipoOperacion;
import com.example.LogiTrack.repository.BodegaRepository;
import com.example.LogiTrack.repository.InventarioRepository;
import com.example.LogiTrack.repository.ProductoRepository;
import com.example.LogiTrack.service.AuditoriaService;
import com.example.LogiTrack.service.InventarioService;
import com.example.LogiTrack.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final InventarioMapper inventarioMapper;
    private final BodegaRepository bodegaRepository;
    private final BodegaMapper bodegaMapper;
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaMapper categoriaMapper;
    private final UsuarioMapper usuarioMapper;
    private final AuditoriaService auditoriaService;
    private final JsonUtil jsonUtil;

    private BodegaDtoResponse toBodegaDto(Bodega b) {
        return bodegaMapper.entityToDto(b,
                b.getUsuarioEncargado() != null ? usuarioMapper.entityToDto(b.getUsuarioEncargado()) : null);
    }

    private ProductoDtoResponse toProductoDto(Producto p) {
        return productoMapper.entityToDto(p, categoriaMapper.entityToDto(p.getCategoria()));
    }

    @Override
    public InventarioDtoResponse guardar(InventarioDtoRequest dto) {
        Bodega bodega = bodegaRepository.findById(dto.bodegaId())
                .orElseThrow(() -> new DatabaseRuleException("Bodega no encontrada"));
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new DatabaseRuleException("Producto no encontrado"));
        Inventario inventario = inventarioMapper.dtoToEntity(dto, bodega, producto);
        inventarioRepository.save(inventario);
        auditoriaService.registrar(TipoOperacion.insert, "inventario", inventario.getId(), null, null, jsonUtil.toJson(inventario));
        return inventarioMapper.entityToDto(inventario, toBodegaDto(bodega), toProductoDto(producto));
    }

    @Override
    public InventarioDtoResponse actualizar(InventarioDtoRequest dto, Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Inventario no encontrado"));
        String antes = jsonUtil.toJson(inventario);
        inventarioMapper.updateEntityFromDto(inventario, dto);
        inventarioRepository.save(inventario);
        auditoriaService.registrar(TipoOperacion.update, "inventario", inventario.getId(), null, antes, jsonUtil.toJson(inventario));
        return inventarioMapper.entityToDto(inventario, toBodegaDto(inventario.getBodega()), toProductoDto(inventario.getProducto()));
    }

    @Override
    public void eliminar(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Inventario no encontrado"));
        auditoriaService.registrar(TipoOperacion.delete, "inventario", inventario.getId(), null, jsonUtil.toJson(inventario), null);
        inventarioRepository.delete(inventario);
    }

    @Override
    public List<InventarioDtoResponse> listar() {
        return inventarioRepository.findAll().stream().map(i ->
                inventarioMapper.entityToDto(i, toBodegaDto(i.getBodega()), toProductoDto(i.getProducto()))
        ).toList();
    }

    @Override
    public InventarioDtoResponse buscarId(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Inventario no encontrado"));
        return inventarioMapper.entityToDto(inventario, toBodegaDto(inventario.getBodega()), toProductoDto(inventario.getProducto()));
    }

    @Override
    public List<InventarioDtoResponse> buscarPorBodega(Long bodegaId) {
        return inventarioRepository.findByBodegaId(bodegaId).stream().map(i ->
                inventarioMapper.entityToDto(i, toBodegaDto(i.getBodega()), toProductoDto(i.getProducto()))
        ).toList();
    }

    @Override
    public List<InventarioDtoResponse> stockBajo() {
        return inventarioRepository.findStockBajo().stream().map(i ->
                inventarioMapper.entityToDto(i, toBodegaDto(i.getBodega()), toProductoDto(i.getProducto()))
        ).toList();
    }

    @Override
    public List<Map<String, Object>> stockTotalPorBodega() {
        return inventarioRepository.stockTotalPorBodega().stream().map(row -> {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("bodega", row[0]);
            mapa.put("stockTotal", row[1]);
            return mapa;
        }).toList();
    }
}
