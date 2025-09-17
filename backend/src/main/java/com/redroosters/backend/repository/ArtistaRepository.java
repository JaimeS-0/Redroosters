package com.redroosters.backend.repository;

import com.redroosters.backend.model.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    // Para mostrar artistas destacados en la home
    List<Artista> findByDestacadoTrue();

    // Buscar 6 artistas random para la pagina de artistas
    @Query(value = """
        SELECT * FROM artistas
        WHERE (:#{#excludeIds == null || #excludeIds.isEmpty()} = true OR id NOT IN (:excludeIds))
        ORDER BY random()
        LIMIT :size
        """, nativeQuery = true)
    List<Artista> findRandom(@Param("size") int size, @Param("excludeIds") List<Long> excludeIds);

}
