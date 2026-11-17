package com.example.LogiTrack.service;

import com.example.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.example.LogiTrack.modelo.TipoOperacion;
import com.example.LogiTrack.modelo.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuditoriaService {
    List<AuditoriaDtoResponse> listar();
    AuditoriaDtoResponse buscarId(Long id);
    List<AuditoriaDtoResponse> buscarPorUsuario(Long usuarioId);
    List<AuditoriaDtoResponse> buscarPorTipoOperacion(TipoOperacion tipoOperacion);

    // Método que llaman todos los services para registrar cambios automáticamente
    void registrar(TipoOperacion operacion, String entidad, Long entidadId,
                   Usuario usuario, String valoresAntes, String valoresDespues);
}
