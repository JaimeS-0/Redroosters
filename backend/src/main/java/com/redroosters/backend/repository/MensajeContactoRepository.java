package com.redroosters.backend.repository;

import com.redroosters.backend.model.MensajeContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositio JPA para la entidad Mensaje de COntacto

@Repository
public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long> {
}
