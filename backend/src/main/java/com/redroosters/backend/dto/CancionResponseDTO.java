package com.redroosters.backend.dto;

// Mostrar Album

public record CancionResponseDTO(

        Long id,
        String titulo,
        String descripcion,
        Integer duracionSegundos,
        String duracionTexto,
        String portadaUrl,
        String urlAudio,
        String nombreArtista,
        String tituloAlbum // puede ser null
) {
}
