package com.redroosters.backend.repository;

import com.redroosters.backend.model.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

// Repositio JPA para la entidad Like y consultas personalizadas

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Devuleve todas las canciones marcadas como Like
    Page<Like> findByUsuarioId(Long usuarioId, Pageable pageable);

    // Busca si exite un like en una cancion para ver si aparece activo
    Optional<Like> findByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Ver si ha dado like a esa cancion (evita duplicados)
    boolean existsByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Elimina el like sobre esa cancion
    void deleteByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Contar los likes completos para mostrarlos
    long countByCancionId(Long cancionId);

    // Cuenta los likes que tienen todas las canciones de un artista
    long countByCancion_Artista_Id(Long artistaId);

    // TOP global por likes (estadisticas)
    @Query("""
           select l.cancion.id as cancionId, count(l) as total
           from Like l
           group by l.cancion.id
           order by total desc
           """)
    List<TopLikeProjection> topGlobalLikes(org.springframework.data.domain.Pageable pageable);

    interface TopLikeProjection {
        Long getCancionId();
        Long getTotal();
    }


}
