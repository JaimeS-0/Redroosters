package com.redroosters.backend.dto;

// Se usa para Mostrar un artista

public record ArtistaResponseDTO(

        Long id,
        String nombre,
        String descripcion,
        String portadaUrl,
        boolean destacado
) {}
