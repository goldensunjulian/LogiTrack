package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.UsuarioDtoRequest;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.exception.BussinesRuleException;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.UsuarioMapper;
import com.example.LogiTrack.modelo.Usuario;
import com.example.LogiTrack.repository.UsuarioRepository;
import com.example.LogiTrack.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioDtoResponse guardar(UsuarioDtoRequest dto) {
        if (usuarioRepository.existsByEmail(dto.email()))
            throw new BussinesRuleException("El email ya está registrado");
        Usuario usuario = usuarioMapper.dtoToEntity(dto);
        return usuarioMapper.entityToDto(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioDtoResponse actualizar(UsuarioDtoRequest dto, Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Usuario no encontrado"));
        usuarioMapper.updateEntityFromDto(usuario, dto);
        return usuarioMapper.entityToDto(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(Long id) {
        // En este sistema no se borra el usuario, se desactiva para mantener integridad de auditoría
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
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
