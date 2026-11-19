package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.InventarioDtoRequest;
import com.example.LogiTrack.dto.response.InventarioDtoResponse;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.BodegaMapper;
import com.example.LogiTrack.mapper.CategoriaMapper;
import com.example.LogiTrack.mapper.InventarioMapper;
import com.example.LogiTrack.mapper.ProductoMapper;
import com.example.LogiTrack.mapper.UsuarioMapper;
import com.example.LogiTrack.modelo.Bodega;
import com.example.LogiTrack.modelo.Inventario;
import com.example.LogiTrack.modelo.Producto;
import com.example.LogiTrack.repository.BodegaRepository;
import com.example.LogiTrack.repository.InventarioRepository;
import com.example.LogiTrack.repository.ProductoRepository;
import com.example.LogiTrack.service.InventarioService;
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

    @Override
    public InventarioDtoResponse guardar(InventarioDtoRequest dto) {
        Bodega bodega = bodegaRepository.findById(dto.bodegaId())
                .orElseThrow(() -> new DatabaseRuleException("Bodega no encontrada"));
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new DatabaseRuleException("Producto no encontrado"));
        Inventario inventario = inventarioMapper.dtoToEntity(dto, bodega, producto);
        inventarioRepository.save(inventario);
        return inventarioMapper.entityToDto(
                inventario,
                bodegaMapper.entityToDto(bodega, usuarioMapper.entityToDto(bodega.getUsuarioEncargado())),
                productoMapper.entityToDto(producto, categoriaMapper.entityToDto(producto.getCategoria()))
        );
    }

    @Override
    public InventarioDtoResponse actualizar(InventarioDtoRequest dto, Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Inventario no encontrado"));
        inventarioMapper.updateEntityFromDto(inventario, dto);
        inventarioRepository.save(inventario);
        return inventarioMapper.entityToDto(
                inventario,
                bodegaMapper.entityToDto(inventario.getBodega(), usuarioMapper.entityToDto(inventario.getBodega().getUsuarioEncargado())),
                productoMapper.entityToDto(inventario.getProducto(), categoriaMapper.entityToDto(inventario.getProducto().getCategoria()))
        );
    }

    @Override
    public void eliminar(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Inventario no encontrado"));
        inventarioRepository.delete(inventario);
    }

    @Override
    public List<InventarioDtoResponse> listar() {
        return inventarioRepository.findAll().stream().map(i ->
                inventarioMapper.entityToDto(
                        i,
                        bodegaMapper.entityToDto(i.getBodega(), usuarioMapper.entityToDto(i.getBodega().getUsuarioEncargado())),
                        productoMapper.entityToDto(i.getProducto(), categoriaMapper.entityToDto(i.getProducto().getCategoria()))
                )
        ).toList();
    }

    @Override
    public InventarioDtoResponse buscarId(Long id) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Inventario no encontrado"));
        return inventarioMapper.entityToDto(
                inventario,
                bodegaMapper.entityToDto(inventario.getBodega(), usuarioMapper.entityToDto(inventario.getBodega().getUsuarioEncargado())),
                productoMapper.entityToDto(inventario.getProducto(), categoriaMapper.entityToDto(inventario.getProducto().getCategoria()))
        );
    }

    @Override
    public List<InventarioDtoResponse> buscarPorBodega(Long bodegaId) {
        return inventarioRepository.findByBodegaId(bodegaId).stream().map(i ->
                inventarioMapper.entityToDto(
                        i,
                        bodegaMapper.entityToDto(i.getBodega(), usuarioMapper.entityToDto(i.getBodega().getUsuarioEncargado())),
                        productoMapper.entityToDto(i.getProducto(), categoriaMapper.entityToDto(i.getProducto().getCategoria()))
                )
        ).toList();
    }

    @Override
    public List<InventarioDtoResponse> stockBajo() {
        return inventarioRepository.findStockBajo().stream().map(i ->
                inventarioMapper.entityToDto(
                        i,
                        bodegaMapper.entityToDto(i.getBodega(), usuarioMapper.entityToDto(i.getBodega().getUsuarioEncargado())),
                        productoMapper.entityToDto(i.getProducto(), categoriaMapper.entityToDto(i.getProducto().getCategoria()))
                )
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