package com.redroosters.backend.repository;

import com.redroosters.backend.model.Cancion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

    // Todas las canciones que pertenecen a un artista concreto,
    Page<Cancion> findByArtistaId(Long artistaId, Pageable pageable);

    // Todas las canciones que están asociadas a un álbum concreto.
    Page<Cancion> findByAlbumId(Long albumId, Pageable pageable);


    // Devuelve todas las canciones de un artista que aun no esten asociadas a ningun album.
    // Al crear un ALbum muestra solo las canciones "libres" o sin album asociado.
    Page<Cancion> findByArtistaIdAndAlbumIsNull(Long artistaId, Pageable pageable);

    // Para el buscador (la q la abrebiatura de query que contega la misma palabra que buscas)
    Page<Cancion> findByTituloContainingIgnoreCase(String q, Pageable pageable);


}
