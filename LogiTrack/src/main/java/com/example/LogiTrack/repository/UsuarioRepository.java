package com.example.LogiTrack.repository;

import com.example.LogiTrack.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByActivoTrue();
    boolean existsByEmail(String email);



    @Query( value = "SELECT u.* FROM usuario u " +
                    "INNER JOIN credenciales c ON u.id = c.usuario_fk " +
                    "INNER JOIN rol r ON c.rol_fk = r.id " +
                    "WHERE r.nombre = :nombreRol",
            nativeQuery = true)
    List<Usuario> findByRolNombre(@Param("nombreRol") String nombreRol);
}
