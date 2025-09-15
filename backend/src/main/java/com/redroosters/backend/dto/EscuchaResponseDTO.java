package com.redroosters.backend.dto;

import java.time.LocalDateTime;

// Mostrar estadisticas

public record EscuchaResponseDTO(

        Long id,
        String nombreUsuario,
        String tituloCancion,
        Long vecesEscuchada,
        LocalDateTime ultimaEscucha
) {}
