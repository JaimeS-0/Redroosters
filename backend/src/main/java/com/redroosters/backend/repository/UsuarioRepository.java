package com.redroosters.backend.repository;

import com.redroosters.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Para registrar usuario
    Optional<Usuario> findByEmail(String email);

    // Buscar por email (login)
    boolean existsByEmail(String email);
}
