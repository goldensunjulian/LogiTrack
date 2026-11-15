package com.example.LogiTrack.mapper;

import com.example.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.example.LogiTrack.dto.response.UsuarioDtoResponse;
import com.example.LogiTrack.modelo.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaDtoResponse entityToDto(Auditoria auditoria, UsuarioDtoResponse usuarioDto) {
        if (auditoria == null || usuarioDto == null) return null;
        return new AuditoriaDtoResponse(
                auditoria.getId(),
                auditoria.getTipoOperacion(),
                auditoria.getFecha(),
                usuarioDto,
                auditoria.getEntidad(),
                auditoria.getEntidadId(),
                auditoria.getValoresAntes(),
                auditoria.getValoresDespues()
        );
    }

    // No tiene dtoToEntity ni updateEntityFromDto porque la auditoría
    // se crea únicamente de forma automática por el sistema, nunca desde un request
}
