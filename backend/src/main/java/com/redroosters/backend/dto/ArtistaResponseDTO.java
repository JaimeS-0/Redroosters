package com.redroosters.backend.dto;

// Se usa para Mostrar un artista

public record ArtistaResponseDTO(

        Long id,
        String nombre,
        String urlNombre,
        String descripcion,
        String portadaUrl,
        boolean destacado,
        long totalEscuchas

) {}
