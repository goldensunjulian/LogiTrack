package com.example.LogiTrack.service.impl;

import com.example.LogiTrack.dto.request.BodegaDtoRequest;
import com.example.LogiTrack.dto.response.BodegaDtoResponse;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.exception.BussinesRuleException;
import com.example.LogiTrack.exception.DatabaseRuleException;
import com.example.LogiTrack.mapper.BodegaMapper;
import com.example.LogiTrack.mapper.UsuarioMapper;
import com.example.LogiTrack.modelo.Bodega;
import com.example.LogiTrack.modelo.Usuario;
import com.example.LogiTrack.repository.BodegaRepository;
import com.example.LogiTrack.repository.UsuarioRepository;
import com.example.LogiTrack.service.BodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;
    private final BodegaMapper bodegaMapper;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    private BodegaDtoResponse toDto(Bodega b) {
        UsuarioDtoResponse encargadoDto = b.getUsuarioEncargado() != null ?
                usuarioMapper.entityToDto(b.getUsuarioEncargado()) : null;
        return bodegaMapper.entityToDto(b, encargadoDto);
    }

    @Override
    public BodegaDtoResponse guardar(BodegaDtoRequest dto) {
        if (bodegaRepository.existsByNombre(dto.nombre()))
            throw new BussinesRuleException("Ya existe una bodega con ese nombre");
        Usuario encargado = null;
        if (dto.usuarioEncargadoId() != null) {
            encargado = usuarioRepository.findById(dto.usuarioEncargadoId())
                    .orElseThrow(() -> new DatabaseRuleException("Usuario encargado no encontrado"));
        }
        Bodega bodega = bodegaMapper.dtoToEntity(dto, encargado);
        bodegaRepository.save(bodega);
        return toDto(bodega);
    }

    @Override
    public BodegaDtoResponse actualizar(BodegaDtoRequest dto, Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Bodega no encontrada"));
        Usuario encargado = null;
        if (dto.usuarioEncargadoId() != null) {
            encargado = usuarioRepository.findById(dto.usuarioEncargadoId())
                    .orElseThrow(() -> new DatabaseRuleException("Usuario encargado no encontrado"));
        }
        bodegaMapper.updateEntityFromDto(bodega, dto, encargado);
        bodegaRepository.save(bodega);
        return toDto(bodega);
    }

    @Override
    public void eliminar(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Bodega no encontrada"));
        bodega.setActivado(false);
        bodegaRepository.save(bodega);
    }

    @Override
    public void activar(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Bodega no encontrada"));
        bodega.setActivado(true);
        bodegaRepository.save(bodega);
    }

    @Override
    public List<BodegaDtoResponse> listar() {
        return bodegaRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public BodegaDtoResponse buscarId(Long id) {
        return toDto(bodegaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Bodega no encontrada")));
    }

    @Override
    public List<BodegaDtoResponse> listarActivas() {
        return bodegaRepository.findByActivadoTrue().stream().map(this::toDto).toList();
    }
}