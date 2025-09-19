package com.redroosters.backend.dto;

// dto para el buscador de artistas canciones y albumes

public record BusquedaDTO(
        Long id,
        String tipo,
        String titulo,
        String subtitulo
) {}
