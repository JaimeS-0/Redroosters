package com.redroosters.backend.dto;

import jakarta.validation.constraints.NotNull;

// Dar like a una cancion

public record LikeRequestDTO(

   @NotNull Long usuarioId,
   @NotNull Long cancionId
) {}
