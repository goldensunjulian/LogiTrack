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

    @Override
    public BodegaDtoResponse guardar(BodegaDtoRequest dto) {
        if (bodegaRepository.existsByNombre(dto.nombre()))
            throw new BussinesRuleException("Ya existe una bodega con ese nombre");
        Usuario encargado = null;
        UsuarioDtoResponse encargadoDto = null;
        if (dto.usuarioEncargadoId() != null) {
            encargado = usuarioRepository.findById(dto.usuarioEncargadoId())
                    .orElseThrow(() -> new DatabaseRuleException("Usuario encargado no encontrado"));
            encargadoDto = usuarioMapper.entityToDto(encargado);
        }
        Bodega bodega = bodegaMapper.dtoToEntity(dto, encargado);
        return bodegaMapper.entityToDto(bodegaRepository.save(bodega), encargadoDto);
    }

    @Override
    public BodegaDtoResponse actualizar(BodegaDtoRequest dto, Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Bodega no encontrada"));
        Usuario encargado = null;
        UsuarioDtoResponse encargadoDto = null;
        if (dto.usuarioEncargadoId() != null) {
            encargado = usuarioRepository.findById(dto.usuarioEncargadoId())
                    .orElseThrow(() -> new DatabaseRuleException("Usuario encargado no encontrado"));
            encargadoDto = usuarioMapper.entityToDto(encargado);
        }
        bodegaMapper.updateEntityFromDto(bodega, dto, encargado);
        return bodegaMapper.entityToDto(bodegaRepository.save(bodega), encargadoDto);
    }

    @Override
    public void eliminar(Long id) {
        // Soft delete — se desactiva para no romper el historial de movimientos
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Bodega no encontrada"));
        bodega.setActivado(false);
        bodegaRepository.save(bodega);
    }

    @Override
    public List<BodegaDtoResponse> listar() {
        return bodegaRepository.findAll().stream().map(b -> {
            UsuarioDtoResponse encargadoDto = b.getUsuarioEncargado() != null ?
                    usuarioMapper.entityToDto(b.getUsuarioEncargado()) : null;
            return bodegaMapper.entityToDto(b, encargadoDto);
        }).toList();
    }

    @Override
    public BodegaDtoResponse buscarId(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new DatabaseRuleException("Bodega no encontrada"));
        UsuarioDtoResponse encargadoDto = bodega.getUsuarioEncargado() != null ?
                usuarioMapper.entityToDto(bodega.getUsuarioEncargado()) : null;
        return bodegaMapper.entityToDto(bodega, encargadoDto);
    }

    @Override
    public List<BodegaDtoResponse> listarActivas() {
        return bodegaRepository.findByActivadoTrue().stream().map(b -> {
            UsuarioDtoResponse encargadoDto = b.getUsuarioEncargado() != null ?
                    usuarioMapper.entityToDto(b.getUsuarioEncargado()) : null;
            return bodegaMapper.entityToDto(b, encargadoDto);
        }).toList();
    }
}
