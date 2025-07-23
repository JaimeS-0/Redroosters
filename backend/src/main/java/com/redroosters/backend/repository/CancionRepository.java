package com.redroosters.backend.repository;

import com.redroosters.backend.model.Cancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

    // Todas las canciones que pertenecen a un artista concreto,
    List<Cancion> findByArtistaId(Long artistaId);

    // Todas las canciones que están asociadas a un álbum concreto.
    List<Cancion> findByAlbumId(Long albumId);

    // Devuelve todas las canciones de un artista que aun no esten asociadas a ningun album.
    // Al crear un ALbum muestra solo las canciones "libres" o sin album asociado.
    List<Cancion> findByArtistaIdAndAlbumIsNull(Long artistaId);

}
