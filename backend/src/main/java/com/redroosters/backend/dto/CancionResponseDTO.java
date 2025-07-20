package com.redroosters.backend.dto;

// Mostrar Album

public record CancionResponseDTO(

        Long id,
        String titulo,
        String descripcion,
        String duracion,
        String portadaUrl,
        String urlAudio,
        String nombreArtista,
        String tituloAlbum // puede ser null
) {
}
