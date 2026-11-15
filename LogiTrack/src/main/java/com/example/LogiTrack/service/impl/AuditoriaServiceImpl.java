package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.AuditoriaMapper;
import com.example.LogiTrack.mapper.UsuarioMapper;
import com.example.LogiTrack.modelo.Auditoria;
import com.example.LogiTrack.modelo.TipoOperacion;
import com.example.LogiTrack.repository.AuditoriaRepository;
import com.example.LogiTrack.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;
    private final UsuarioMapper usuarioMapper;

    @Override
    public List<AuditoriaDtoResponse> listar() {
        return auditoriaRepository.findAll().stream().map(a ->
                auditoriaMapper.entityToDto(a, usuarioMapper.entityToDto(a.getUsuario()))
        ).toList();
    }

    @Override
    public AuditoriaDtoResponse buscarId(Long id) {
        Auditoria auditoria = auditoriaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Auditoría no encontrada"));
        return auditoriaMapper.entityToDto(auditoria, usuarioMapper.entityToDto(auditoria.getUsuario()));
    }

    @Override
    public List<AuditoriaDtoResponse> buscarPorUsuario(Long usuarioId) {
        return auditoriaRepository.findByUsuarioId(usuarioId).stream().map(a ->
                auditoriaMapper.entityToDto(a, usuarioMapper.entityToDto(a.getUsuario()))
        ).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarPorTipoOperacion(TipoOperacion tipoOperacion) {
        return auditoriaRepository.findByTipoOperacion(tipoOperacion).stream().map(a ->
                auditoriaMapper.entityToDto(a, usuarioMapper.entityToDto(a.getUsuario()))
        ).toList();
    }
}
