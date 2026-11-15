package com.example.LogiTrack.service;

import com.example.LogiTrack.dto.request.MovimientoDetalleDtoRequest;
import com.example.LogiTrack.dto.response.MovimientoDetalleDtoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MovimientoDetalleService {
    MovimientoDetalleDtoResponse guardar(MovimientoDetalleDtoRequest dto);
    void eliminar(Long id);
    List<MovimientoDetalleDtoResponse> listar();
    MovimientoDetalleDtoResponse buscarId(Long id);
    List<MovimientoDetalleDtoResponse> buscarPorMovimiento(Long movimientoId);
    List<MovimientoDetalleDtoResponse> buscarPorProducto(Long productoId);
    List<Map<String, Object>> productosMasMovidos();
}
