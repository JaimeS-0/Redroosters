package com.redroosters.backend.repository;

import com.redroosters.backend.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Ver todos los albumes de un artista
    List<Album> findByArtistaId(Long artistaId);
}
