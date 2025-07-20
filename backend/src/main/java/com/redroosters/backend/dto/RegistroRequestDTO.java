package com.redroosters.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Para registrar nuevos usuarios normales(rol USER)

public record RegistroRequestDTO(

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 50)
        String username,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe ser un email válido")
        String email,

        @NotBlank(message = "La contrasela es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password

        // El campo confirmar password se hace solo en el frontend
) {}
