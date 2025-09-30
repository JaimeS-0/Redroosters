package com.redroosters.backend.repository;

import com.redroosters.backend.model.Cancion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositio JPA para la entidad Cancion y consultas personalizadas

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


    // Para estadisticas, Busca una canción por su ID y carga también el artista en la misma consulta (evita LazyInitializationException).
    @Query("""
       select c from Cancion c
       join fetch c.artista a
       where c.id = :id
       """)
    Optional<Cancion> findByIdWithArtista(Long id);

}
