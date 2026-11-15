package com.example.LogiTrack.repository;

import com.example.LogiTrack.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // listar solo productos activos
    List<Producto> findByActivoTrue();

    // listar productos por categoría
    List<Producto> findByCategoriaId(Long categoriaId);

    // por si existe un producto con el mismo nombre
    boolean existsByNombre(String nombre);

    // para validar que no se borre una categoría que tiene productos asociados
    boolean existsByCategoriaId(Long categoriaId);
}
