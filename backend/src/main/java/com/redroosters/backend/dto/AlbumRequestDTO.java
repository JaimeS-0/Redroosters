package com.redroosters.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// Recibe desde el frontend

public record AlbumRequestDTO(

        @NotBlank String titulo,
        String descripcion,
        String portadaUrl,
        @NotNull Long artistaId,
        List<Long> cancionesIds
) {}
