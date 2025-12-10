package com.redroosters.backend.dto;

import java.util.List;

// Mostrar Album al front

public record AlbumResponseDTO(

        Long id,
        String titulo,
        String descripcion,
        String portadaUrl,
        String nombreArtista,
        List<AlbumCancionDTO> canciones,
        Long artistaId,
        int totalCanciones
) {}
