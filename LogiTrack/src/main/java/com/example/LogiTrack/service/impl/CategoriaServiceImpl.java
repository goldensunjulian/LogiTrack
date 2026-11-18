package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.CategoriaDtoRequest;
import com.example.LogiTrack.dto.response.CategoriaDtoResponse;
import com.example.LogiTrack.exception.BussinesRuleException;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.CategoriaMapper;
import com.example.LogiTrack.modelo.Categoria;
import com.example.LogiTrack.repository.CategoriaRepository;
import com.example.LogiTrack.repository.ProductoRepository;
import com.example.LogiTrack.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final ProductoRepository productoRepository;

    @Override
    public CategoriaDtoResponse guardar(CategoriaDtoRequest dto) {
        if (categoriaRepository.existsByNombre(dto.nombre()))
            throw new BussinesRuleException("Ya existe una categoría con ese nombre");
        Categoria categoria = categoriaMapper.dtoToEntity(dto);
        categoriaRepository.save(categoria);
        return categoriaMapper.entityToDto(categoria);
    }

    @Override
    public CategoriaDtoResponse actualizar(CategoriaDtoRequest dto, Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Categoría no encontrada"));
        categoriaMapper.updateEntityFromDto(categoria, dto);
        categoriaRepository.save(categoria);
        return categoriaMapper.entityToDto(categoria);
    }

    @Override
    public void eliminar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Categoría no encontrada"));
        if (productoRepository.existsByCategoriaId(id))
            throw new BussinesRuleException("No se puede eliminar la categoría porque tiene productos asociados");
        categoriaRepository.delete(categoria);
    }

    @Override
    public List<CategoriaDtoResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::entityToDto).toList();
    }

    @Override
    public CategoriaDtoResponse buscarId(Long id) {
        return categoriaMapper.entityToDto(categoriaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Categoría no encontrada")));
    }
}