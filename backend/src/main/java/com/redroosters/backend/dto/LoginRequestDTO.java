package com.redroosters.backend.dto;

// Para inisiar sesion

public record LoginRequestDTO(

        String email,
        String password
) {}
