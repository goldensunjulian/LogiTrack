package com.example.LogiTrack.service;

import com.example.LogiTrack.dto.request.MovimientoDtoRequest;
import com.example.LogiTrack.dto.response.MovimientoDtoResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface MovimientoService {
    MovimientoDtoResponse guardar(MovimientoDtoRequest dto);
    void eliminar(Long id);
    List<MovimientoDtoResponse> listar();
    MovimientoDtoResponse buscarId(Long id);
    List<MovimientoDtoResponse> buscarPorUsuario(Long usuarioId);
    List<MovimientoDtoResponse> buscarPorTipo(String tipoMovimiento);
    List<MovimientoDtoResponse> buscarPorFechas(LocalDateTime inicio, LocalDateTime fin);
}
