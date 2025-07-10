package com.redroosters.backend.repository;

import com.redroosters.backend.model.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    // Para mostrar artistas destacados en la home
    List<Artista> findByDestacadoTrue();
}
