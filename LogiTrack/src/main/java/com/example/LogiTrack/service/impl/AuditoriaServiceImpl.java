package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.AuditoriaMapper;
import com.example.LogiTrack.mapper.UsuarioMapper;
import com.example.LogiTrack.modelo.Auditoria;
import com.example.LogiTrack.modelo.TipoOperacion;
import com.example.LogiTrack.modelo.Usuario;
import com.example.LogiTrack.repository.AuditoriaRepository;
import com.example.LogiTrack.repository.CredencialesRepository;
import com.example.LogiTrack.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;
    private final UsuarioMapper usuarioMapper;
    private final CredencialesRepository credencialesRepository;

    // Obtiene el usuario logueado desde el token JWT
    private Usuario getUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return credencialesRepository.findByUsername(username).getUsuario();
    }

    @Override
    public void registrar(TipoOperacion operacion, String entidad, Long entidadId,
                          Usuario usuario, String valoresAntes, String valoresDespues) {
        Auditoria auditoria = new Auditoria();
        auditoria.setTipoOperacion(operacion);
        auditoria.setEntidad(entidad);
        auditoria.setEntidadId(entidadId);
        auditoria.setUsuario(usuario != null ? usuario : getUsuarioActual());
        auditoria.setValoresAntes(valoresAntes);
        auditoria.setValoresDespues(valoresDespues);
        auditoriaRepository.save(auditoria);
    }

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
