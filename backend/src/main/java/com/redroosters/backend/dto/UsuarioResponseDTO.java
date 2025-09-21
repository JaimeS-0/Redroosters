package com.redroosters.backend.dto;

import com.redroosters.backend.model.Role;

// Devuelve informacion del usuario

public record UsuarioResponseDTO(

        Long id,
        String name,
        String email,
        Role role
) {}
