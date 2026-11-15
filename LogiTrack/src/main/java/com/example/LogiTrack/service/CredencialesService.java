package com.example.LogiTrack.service;

import com.example.LogiTrack.dto.request.CredencialesDtoRequest;
import com.example.LogiTrack.dto.response.CredencialesDtoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CredencialesService {
    CredencialesDtoResponse guardar(CredencialesDtoRequest dto);
    CredencialesDtoResponse actualizar(CredencialesDtoRequest dto, Long id);
    void eliminar(Long id);
    void eliminarPorUsuario(Long usuarioId);
    List<CredencialesDtoResponse> listar();
    CredencialesDtoResponse buscarId(Long id);
}
