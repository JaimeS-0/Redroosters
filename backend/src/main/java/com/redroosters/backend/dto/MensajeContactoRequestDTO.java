package com.redroosters.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Cuando alguien envvia eñ formulario de contacto al servidor

public record MensajeContactoRequestDTO(

        @NotBlank(message = "EL nombre es obligatorio")
        @Size(min = 2, max = 50)
        String nombre,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe ser un email Válido")
        String email,

        @NotBlank(message = "El mensaje no puede estar vacío")
        @Size(min = 5, max = 1000)
        String mensaje
) {}
