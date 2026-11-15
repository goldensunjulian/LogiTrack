package com.example.LogiTrack.repository;

import com.example.LogiTrack.modelo.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Long> {
    // Listar solo bodegas activas
    List<Bodega> findByActivadoTrue();

    // esta sirve para:
    // buscar la bodega donde un usuario es encargado 
    // para validar que solo el encargado registre movimientos
    Bodega findByUsuarioEncargadoId(Long usuarioId);

    // Validar que no exista ya una bodega con el mismo nombre
    boolean existsByNombre(String nombre);
}
