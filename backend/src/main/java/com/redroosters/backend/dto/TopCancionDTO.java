package com.redroosters.backend.dto;

// Mostrar top 10 canciones de la web por escuchas

public record TopCancionDTO(
        Long cancionId,
        String titulo,
        String artista,
        Long escuchasTotales,
        String portadaUrl
) {}
