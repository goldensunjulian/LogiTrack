package com.example.LogiTrack.repository;

import com.example.LogiTrack.modelo.Auditoria;
import com.example.LogiTrack.modelo.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    // Para buscar auditorias por usuario
    List<Auditoria> findByUsuarioId(Long usuarioId);

    // Para buscar auditorias por tipo de operacion xdxd
    List<Auditoria> findByTipoOperacion(TipoOperacion tipoOperacion);
}
