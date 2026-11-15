package com.example.LogiTrack.service;

import com.example.LogiTrack.dto.request.CategoriaDtoRequest;
import com.example.LogiTrack.dto.response.CategoriaDtoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoriaService {
    CategoriaDtoResponse guardar(CategoriaDtoRequest dto);
    CategoriaDtoResponse actualizar(CategoriaDtoRequest dto, Long id);
    void eliminar(Long id);
    List<CategoriaDtoResponse> listar();
    CategoriaDtoResponse buscarId(Long id);
}
