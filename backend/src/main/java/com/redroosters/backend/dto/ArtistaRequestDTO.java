package com.redroosters.backend.dto;

import jakarta.validation.constraints.NotBlank;

// Se usa para Crear o Editar un artista

public record ArtistaRequestDTO(

        @NotBlank String nombre,
        String descripcion,
        String portadaUrl,
        boolean destacado
) {}
