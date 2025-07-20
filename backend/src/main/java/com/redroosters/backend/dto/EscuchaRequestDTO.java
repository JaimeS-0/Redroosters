package com.redroosters.backend.dto;

import jakarta.validation.constraints.NotNull;

// Registrar nueva escucha (cuando reproduce la cancion)

public record EscuchaRequestDTO(

        @NotNull Long usuarioId,
        @NotNull Long cancionId
) {}
