package com.redroosters.backend.dto;

// Mostrar Album

public record CancionResponseDTO(

        Long id,
        String titulo,
        String descripcion,
        Integer duracionSegundos,
        String duracionTexto,
        String portada,
        String urlAudio,
        String nombreArtista,
        String tituloAlbum // puede ser null
) {
}
