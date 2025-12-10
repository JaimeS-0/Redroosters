package com.redroosters.backend.dto;

// Detalle de el artista en su pagina

public record ArtistaDetalleDTO(

        Long id,
        String nombre,
        String urlNombre,
        String descripcion,
        String portadaUrl,
        boolean destacado,
        long totalEscuchas,
        long totalLikes,
        long totalCanciones,
        long totalAlbumes
) {}
