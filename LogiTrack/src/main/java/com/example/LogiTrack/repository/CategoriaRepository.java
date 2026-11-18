package com.example.LogiTrack.repository;

import com.example.LogiTrack.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // para ver si ya existe tal categoria
    boolean existsByNombre(String nombre);

}
