package com.redroosters.backend.repository;

import com.redroosters.backend.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Devuleve todas las canciones marcadas como Like
    List<Like> findByUsuarioId(Long usuarioId);

    // Busca si exite un like en una cancion para ver si aparece activo
    Optional<Like> findByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Ver si ha dado like a esa cancion (evita duplicados)
    boolean existsByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Elimina el like sobre esa cancion
    void deleteByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);
}
