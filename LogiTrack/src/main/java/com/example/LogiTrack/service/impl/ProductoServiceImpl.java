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
import com.example.LogiTrack.repository.CategoriaRepository;
import com.example.LogiTrack.repository.ProductoRepository;
import com.example.LogiTrack.service.ProductoService;
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

    @Override
    public ProductoDtoResponse guardar(ProductoDtoRequest dto) {
        if (productoRepository.existsByNombre(dto.nombre()))
            throw new BussinesRuleException("Ya existe un producto con ese nombre");
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new DatabaseRuleException("Categoría no encontrada"));
        Producto producto = productoMapper.dtoToEntity(dto, categoria);
        CategoriaDtoResponse categoriaDto = categoriaMapper.entityToDto(categoria);
        return productoMapper.entityToDto(productoRepository.save(producto), categoriaDto);
    }

    @Override
    public ProductoDtoResponse actualizar(ProductoDtoRequest dto, Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Producto no encontrado"));
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new DatabaseRuleException("Categoría no encontrada"));
        productoMapper.updateEntityFromDto(producto, dto, categoria);
        CategoriaDtoResponse categoriaDto = categoriaMapper.entityToDto(categoria);
        return productoMapper.entityToDto(productoRepository.save(producto), categoriaDto);
    }

    @Override
    public void eliminar(Long id) {
        // Soft delete — se desactiva para no romper el historial de movimientos
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Producto no encontrado"));
        producto.setActivo(false);
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
