package com.redroosters.backend.dto;

import java.time.LocalDateTime;

// Mostrar mensajes en un panel(pensiente)

public record MensajeContactoResponseDTO(

    Long id,
    String nombre,
    String email,
    String mensaje,
    LocalDateTime createdAt
) {}
