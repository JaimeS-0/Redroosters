// src/main/java/com/redroosters/backend/service/EstadisticasService.java
package com.redroosters.backend.service;

import com.redroosters.backend.dto.EstadisticaEscuchaLikeDTO;
import com.redroosters.backend.dto.EstadisticasDTO;
import com.redroosters.backend.model.Cancion;
import com.redroosters.backend.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Lógica para ver las estadísticas en la web en el panel de administrador

@Service
public class EstadisticasService {

    private final CancionRepository cancionRepository;
    private final ArtistaRepository artistaRepository;
    private final AlbumRepository albumRepository;
    private final EscuchaRepository escuchaRepository;
    private final LikeRepository likeRepository;

    public EstadisticasService(CancionRepository cancionRepository,
                               ArtistaRepository artistaRepository,
                               AlbumRepository albumRepository,
                               EscuchaRepository escuchaRepository,
                               LikeRepository likeRepository) {
        this.cancionRepository = cancionRepository;
        this.artistaRepository = artistaRepository;
        this.albumRepository = albumRepository;
        this.escuchaRepository = escuchaRepository;
        this.likeRepository = likeRepository;
    }

    @Transactional(readOnly = true)
    public EstadisticasDTO getStats() {

        long totalCanciones = cancionRepository.count();
        long totalArtistas  = artistaRepository.count();
        long totalAlbumes   = albumRepository.count();
        long totalEscuchas  = escuchaRepository.sumAllEscuchas();

        // TOP por escuchas
        var topEscuchas = escuchaRepository.topGlobal(PageRequest.of(0, 1));
        EstadisticaEscuchaLikeDTO masEscuchada = topEscuchas.isEmpty()
                ? null
                : toTopEntry(topEscuchas.get(0).getCancionId(), topEscuchas.get(0).getTotal());

        // TOP por likes
        var topLikes = likeRepository.topGlobalLikes(PageRequest.of(0, 1));
        EstadisticaEscuchaLikeDTO masLikes = topLikes == null || topLikes.isEmpty()
                ? null
                : toTopEntry(topLikes.get(0).getCancionId(), topLikes.get(0).getTotal());

        return new EstadisticasDTO(
                totalCanciones,
                totalArtistas,
                totalAlbumes,
                totalEscuchas,
                masEscuchada,
                masLikes
        );
    }

    // Convierte (cancionId,total) -> DTO con título y artista
    private EstadisticaEscuchaLikeDTO toTopEntry(Long cancionId, Long total) {
        Cancion c = cancionRepository.findByIdWithArtista(cancionId).orElse(null);
        if (c == null) return null;
        return new EstadisticaEscuchaLikeDTO(
                c.getId(),
                c.getTitulo(),
                c.getArtista().getNombre(),
                total == null ? 0L : total
        );
    }
}
