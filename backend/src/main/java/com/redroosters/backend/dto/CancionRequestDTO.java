package com.redroosters.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Crear o editar desde formulario

public record CancionRequestDTO(

        @NotBlank String titulo,
        String descripcion,
        String portada,
        @NotBlank String urlAudio,
        @NotNull Long artistaId,
        Long albumId // puede ser null si no tiene album
) {}
