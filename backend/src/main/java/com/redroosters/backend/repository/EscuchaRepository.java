package com.redroosters.backend.repository;

import com.redroosters.backend.model.Escucha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EscuchaRepository extends JpaRepository<Escucha, Long> {

    // Busca si un usuario ya ha escuchadp una cancion concreta
    Optional<Escucha> findByUsuarioIdAndCancionId(Long usuarioId, Long cancionId);

    // Devuelve todas las escuchas de un usuario
    List<Escucha> findByUsuarioId(Long usuarioId);

    // Devuelve las 10 canciones mas escuchadas (numero de veces)
    List<Escucha> findTop10ByOrderByVecesEscuchadaDesc();
}
