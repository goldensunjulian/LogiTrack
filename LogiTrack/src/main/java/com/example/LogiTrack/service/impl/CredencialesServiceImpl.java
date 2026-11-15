package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.CredencialesDtoRequest;
import com.example.LogiTrack.dto.response.CredencialesDtoResponse;
import com.example.LogiTrack.dto.response.RolDtoResponse;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.exception.BussinesRuleException;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.CredencialesMapper;
import com.example.LogiTrack.mapper.UsuarioMapper;
import com.example.LogiTrack.modelo.Credenciales;
import com.example.LogiTrack.modelo.Rol;
import com.example.LogiTrack.modelo.Usuario;
import com.example.LogiTrack.repository.CredencialesRepository;
import com.example.LogiTrack.repository.RolRepository;
import com.example.LogiTrack.repository.UsuarioRepository;
import com.example.LogiTrack.service.CredencialesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CredencialesServiceImpl implements CredencialesService {

    private final CredencialesRepository credencialesRepository;
    private final CredencialesMapper credencialesMapper;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Convierte Rol a RolDtoResponse directamente sin mapper
    private RolDtoResponse toRolDto(Rol rol) {
        return new RolDtoResponse(rol.getId(), rol.getNombre());
    }

    @Override
    public CredencialesDtoResponse guardar(CredencialesDtoRequest dto) {
        if (credencialesRepository.existsByUsername(dto.username()))
            throw new BussinesRuleException("El username ya está tomado");
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new DatabaseRuleException("Usuario no encontrado"));
        Rol rol = rolRepository.findById(dto.rolId())
                .orElseThrow(() -> new DatabaseRuleException("Rol no encontrado"));
        Credenciales credenciales = credencialesMapper.dtoToEntity(dto, usuario, rol);
        // Hashear la contraseña antes de guardar
        credenciales.setPassword(passwordEncoder.encode(dto.password()));
        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(usuario);
        return credencialesMapper.entityToDto(credencialesRepository.save(credenciales), usuarioDto, toRolDto(rol));
    }

    @Override
    public CredencialesDtoResponse actualizar(CredencialesDtoRequest dto, Long id) {
        Credenciales credenciales = credencialesRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Credenciales no encontradas"));
        Rol rol = rolRepository.findById(dto.rolId())
                .orElseThrow(() -> new DatabaseRuleException("Rol no encontrado"));
        credencialesMapper.updateEntityFromDto(credenciales, dto, rol);
        UsuarioDtoResponse usuarioDto = usuarioMapper.entityToDto(credenciales.getUsuario());
        return credencialesMapper.entityToDto(credencialesRepository.save(credenciales), usuarioDto, toRolDto(rol));
    }

    @Override
    public void eliminar(Long id) {
        Credenciales credenciales = credencialesRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Credenciales no encontradas"));
        credencialesRepository.delete(credenciales);
    }

    @Override
    public void eliminarPorUsuario(Long usuarioId) {
        // Se llama cuando se despide a un empleado — borra sus credenciales para que no pueda loguearse
        Credenciales credenciales = credencialesRepository.findByUsuarioId(usuarioId);
        if (credenciales == null)
            throw new DatabaseRuleException("No se encontraron credenciales para ese usuario");
        credencialesRepository.delete(credenciales);
    }

    @Override
    public List<CredencialesDtoResponse> listar() {
        return credencialesRepository.findAll().stream().map(c ->
                credencialesMapper.entityToDto(c,
                        usuarioMapper.entityToDto(c.getUsuario()),
                        toRolDto(c.getRol()))
        ).toList();
    }

    @Override
    public CredencialesDtoResponse buscarId(Long id) {
        Credenciales credenciales = credencialesRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Credenciales no encontradas"));
        return credencialesMapper.entityToDto(credenciales,
                usuarioMapper.entityToDto(credenciales.getUsuario()),
                toRolDto(credenciales.getRol()));
    }
}
