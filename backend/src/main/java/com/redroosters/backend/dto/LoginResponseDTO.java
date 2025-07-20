package com.redroosters.backend.dto;


// Lo que devuelve al hacer login

public record LoginResponseDTO(

        String token,
        String username,
        String role
) {}
