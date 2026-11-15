package com.example.LogiTrack.service;

import com.example.LogiTrack.dto.request.ProductoDtoRequest;
import com.example.LogiTrack.dto.response.ProductoDtoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductoService {
    ProductoDtoResponse guardar(ProductoDtoRequest dto);
    ProductoDtoResponse actualizar(ProductoDtoRequest dto, Long id);
    void eliminar(Long id);
    List<ProductoDtoResponse> listar();
    ProductoDtoResponse buscarId(Long id);
    List<ProductoDtoResponse> listarActivos();
    List<ProductoDtoResponse> buscarPorCategoria(Long categoriaId);
}
