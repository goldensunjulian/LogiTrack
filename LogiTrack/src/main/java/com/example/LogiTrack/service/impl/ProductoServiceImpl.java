package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.ProductoDtoRequest;
import com.example.LogiTrack.dto.response.CategoriaDtoResponse;
import com.example.LogiTrack.dto.response.ProductoDtoResponse;
import com.example.LogiTrack.exception.BussinesRuleException;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.CategoriaMapper;
import com.example.LogiTrack.mapper.ProductoMapper;
import com.example.LogiTrack.modelo.Categoria;
import com.example.LogiTrack.modelo.Producto;
import com.example.LogiTrack.modelo.TipoOperacion;
import com.example.LogiTrack.repository.CategoriaRepository;
import com.example.LogiTrack.repository.ProductoRepository;
import com.example.LogiTrack.service.AuditoriaService;
import com.example.LogiTrack.service.ProductoService;
import com.example.LogiTrack.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final AuditoriaService auditoriaService;
    private final JsonUtil jsonUtil;

    @Override
    public ProductoDtoResponse guardar(ProductoDtoRequest dto) {
        if (productoRepository.existsByNombre(dto.nombre()))
            throw new BussinesRuleException("Ya existe un producto con ese nombre");
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new DatabaseRuleException("Categoría no encontrada"));
        Producto producto = productoMapper.dtoToEntity(dto, categoria);
        productoRepository.save(producto);
        return productoMapper.entityToDto(producto, categoriaMapper.entityToDto(categoria));
    }

    @Override
    public ProductoDtoResponse actualizar(ProductoDtoRequest dto, Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Producto no encontrado"));
        String antes = jsonUtil.toJson(producto);
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new DatabaseRuleException("Categoría no encontrada"));
        productoMapper.updateEntityFromDto(producto, dto, categoria);
        productoRepository.save(producto);
        return productoMapper.entityToDto(producto, categoriaMapper.entityToDto(categoria));
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Producto no encontrado"));
        String antes = jsonUtil.toJson(producto);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    public void activar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Producto no encontrado"));
        String antes = jsonUtil.toJson(producto);
        producto.setActivo(true);
        productoRepository.save(producto);
    }

    @Override
    public List<ProductoDtoResponse> listar() {
        return productoRepository.findAll().stream().map(p ->
                productoMapper.entityToDto(p, categoriaMapper.entityToDto(p.getCategoria()))
        ).toList();
    }

    @Override
    public ProductoDtoResponse buscarId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Producto no encontrado"));
        return productoMapper.entityToDto(producto, categoriaMapper.entityToDto(producto.getCategoria()));
    }

    @Override
    public List<ProductoDtoResponse> listarActivos() {
        return productoRepository.findByActivoTrue().stream().map(p ->
                productoMapper.entityToDto(p, categoriaMapper.entityToDto(p.getCategoria()))
        ).toList();
    }

    @Override
    public List<ProductoDtoResponse> buscarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream().map(p ->
                productoMapper.entityToDto(p, categoriaMapper.entityToDto(p.getCategoria()))
        ).toList();
    }
}
