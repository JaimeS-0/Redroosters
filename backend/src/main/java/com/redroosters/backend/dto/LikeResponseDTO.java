package com.redroosters.backend.dto;

import java.time.LocalDateTime;

// Mostrar likes de un usuarioo

public record LikeResponseDTO(

        Long id,
        String nombreUsuario,
        String tituloCancion,
        LocalDateTime fecha
) {
}
