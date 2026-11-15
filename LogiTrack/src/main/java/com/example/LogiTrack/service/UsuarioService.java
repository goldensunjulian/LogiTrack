package com.example.LogiTrack.service;

import com.example.LogiTrack.dto.request.UsuarioDtoRequest;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsuarioService {
    UsuarioDtoResponse guardar(UsuarioDtoRequest dto);
    UsuarioDtoResponse actualizar(UsuarioDtoRequest dto, Long id);
    void eliminar(Long id);
    List<UsuarioDtoResponse> listar();
    UsuarioDtoResponse buscarId(Long id);
    List<UsuarioDtoResponse> listarActivos();
    List<UsuarioDtoResponse> buscarPorRol(String nombreRol);
}
