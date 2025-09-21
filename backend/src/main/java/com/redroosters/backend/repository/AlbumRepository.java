package com.redroosters.backend.repository;

import com.redroosters.backend.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositio JPA para la entidad Album y consultas personalizadas

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Ver todos los albumes de un artista
    Page<Album> findByArtistaId(Long artistaId, Pageable pageable);


}
