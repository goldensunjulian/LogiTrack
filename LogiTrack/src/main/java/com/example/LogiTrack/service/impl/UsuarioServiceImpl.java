package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.UsuarioDtoRequest;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.exception.BussinesRuleException;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.UsuarioMapper;
import com.example.LogiTrack.modelo.TipoOperacion;
import com.example.LogiTrack.modelo.Usuario;
import com.example.LogiTrack.repository.UsuarioRepository;
import com.example.LogiTrack.repository.CredencialesRepository;
import com.example.LogiTrack.service.AuditoriaService;
import com.example.LogiTrack.service.UsuarioService;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.LogiTrack.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final AuditoriaService auditoriaService;
    private final JsonUtil jsonUtil;
    private final CredencialesRepository credencialesRepository;

    // Obtiene el id del usuario logueado desde el token
    private Long getUsuarioLogueadoId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var cred = credencialesRepository.findByUsername(username);
        return cred != null ? cred.getUsuario().getId() : null;
    }

    @Override
    public UsuarioDtoResponse guardar(UsuarioDtoRequest dto) {
        if (usuarioRepository.existsByEmail(dto.email()))
            throw new BussinesRuleException("El email ya está registrado");
        Usuario usuario = usuarioMapper.dtoToEntity(dto);
        usuarioRepository.save(usuario);
        auditoriaService.registrar(TipoOperacion.insert, "usuario", usuario.getId(), null, null, jsonUtil.toJson(usuario));
        return usuarioMapper.entityToDto(usuario);
    }

    @Override
    public UsuarioDtoResponse actualizar(UsuarioDtoRequest dto, Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Usuario no encontrado"));
        String antes = jsonUtil.toJson(usuario);
        usuarioMapper.updateEntityFromDto(usuario, dto);
        usuarioRepository.save(usuario);
        auditoriaService.registrar(TipoOperacion.update, "usuario", usuario.getId(), null, antes, jsonUtil.toJson(usuario));
        return usuarioMapper.entityToDto(usuario);
    }

    @Override
    public void eliminar(Long id) {
        if (id.equals(getUsuarioLogueadoId()))
            throw new BussinesRuleException("No puedes desactivarte a ti mismo");
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Usuario no encontrado"));
        String antes = jsonUtil.toJson(usuario);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        auditoriaService.registrar(TipoOperacion.update, "usuario", usuario.getId(), null, antes, jsonUtil.toJson(usuario));
    }

    @Override
    public void activar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Usuario no encontrado"));
        String antes = jsonUtil.toJson(usuario);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
        auditoriaService.registrar(TipoOperacion.update, "usuario", usuario.getId(), null, antes, jsonUtil.toJson(usuario));
    }

    @Override
    public List<UsuarioDtoResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::entityToDto).toList();
    }

    @Override
    public UsuarioDtoResponse buscarId(Long id) {
        return usuarioMapper.entityToDto(usuarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Usuario no encontrado")));
    }

    @Override
    public List<UsuarioDtoResponse> listarActivos() {
        return usuarioRepository.findByActivoTrue().stream()
                .map(usuarioMapper::entityToDto).toList();
    }

    @Override
    public List<UsuarioDtoResponse> buscarPorRol(String nombreRol) {
        return usuarioRepository.findByRolNombre(nombreRol).stream()
                .map(usuarioMapper::entityToDto).toList();
    }
}
