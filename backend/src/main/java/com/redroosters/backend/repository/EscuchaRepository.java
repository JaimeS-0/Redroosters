package com.redroosters.backend.repository;

import com.redroosters.backend.model.Escucha;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Repositio JPA para la entidad Escucha y consultas personalizadas

@Repository
public interface EscuchaRepository extends JpaRepository<Escucha, Long> {

    // Busca si un usuario ya ha escuchadp una cancion concreta
    Optional<Escucha> findByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Devuelve todas las escuchas de un usuario
    //List<Escucha> findByUsuarioId(Long usuarioId);

    // Contador global por cancion
    @Query("""
           select coalesce(sum(e.vecesEscuchada), 0)
           from Escucha e
           where e.cancion.id = :cancionId
           """)
    Long sumaGlobalPorCancion(Long cancionId);

    // TOP global por cancion
    @Query("""
           select e.cancion.id as cancionId,
                  coalesce(sum(e.vecesEscuchada), 0) as total
           from Escucha e
           group by e.cancion.id
           order by total desc
           """)
    List<TopCancionProjection> topGlobal(Pageable pageable);

    // Suma las veces escuchas totales
    @Query("select coalesce(sum(e.vecesEscuchada), 0) from Escucha e")
    long sumAllEscuchas();

    // Cuenta todas las escuchas de un artista
    @Query("SELECT COUNT(e) FROM Escucha e WHERE e.cancion.artista.id = :artistaId")
    long countByArtistaId(@Param("artistaId") Long artistaId);

    interface TopCancionProjection {
        Long getCancionId();
        Long getTotal();
    }
}
