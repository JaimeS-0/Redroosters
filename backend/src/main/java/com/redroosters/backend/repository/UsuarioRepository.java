package com.redroosters.backend.repository;

import com.redroosters.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositio JPA para la entidad Usuario y consultas personalizadas

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Para buscar un usuario (login)
    Optional<Usuario> findByEmail(String email);

    // Comprobar si ya existe un email para registro (evitar duplicados)
    boolean existsByEmail(String email);

}
