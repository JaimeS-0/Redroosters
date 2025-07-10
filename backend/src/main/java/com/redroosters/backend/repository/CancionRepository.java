package com.redroosters.backend.repository;

import com.redroosters.backend.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

    // Mostrar todas las canciones de un artista en concreto
    List<Cancion> findByArtistaId(Long artistaId);
}
