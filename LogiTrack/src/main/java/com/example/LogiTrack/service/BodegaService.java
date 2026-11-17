package com.example.LogiTrack.service;

import com.example.LogiTrack.dto.request.BodegaDtoRequest;
import com.example.LogiTrack.dto.response.BodegaDtoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BodegaService {
    BodegaDtoResponse guardar(BodegaDtoRequest dto);
    BodegaDtoResponse actualizar(BodegaDtoRequest dto, Long id);
    void eliminar(Long id);
    void activar(Long id);
    List<BodegaDtoResponse> listar();
    BodegaDtoResponse buscarId(Long id);
    List<BodegaDtoResponse> listarActivas();
}
