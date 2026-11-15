package com.example.LogiTrack.repository;

import com.example.LogiTrack.modelo.Credenciales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredencialesRepository extends JpaRepository<Credenciales, Long> {
    // buscar por username para autenticar
    Credenciales findByUsername(String username);

    // validar que el username no esté tomado al registrar
    boolean existsByUsername(String username);

    // buscar credenciales por usuario
    // para cuando se despide a alguien y se borran sus credenciales
    Credenciales findByUsuarioId(Long usuarioId);
}
