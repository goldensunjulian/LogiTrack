package com.example.LogiTrack.service;

import com.example.LogiTrack.dto.request.InventarioDtoRequest;
import com.example.LogiTrack.dto.response.InventarioDtoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface InventarioService {
    InventarioDtoResponse guardar(InventarioDtoRequest dto);
    InventarioDtoResponse actualizar(InventarioDtoRequest dto, Long id);
    void eliminar(Long id);
    List<InventarioDtoResponse> listar();
    InventarioDtoResponse buscarId(Long id);
    List<InventarioDtoResponse> buscarPorBodega(Long bodegaId);
    List<InventarioDtoResponse> stockBajo();
    List<Map<String, Object>> stockTotalPorBodega();
}
