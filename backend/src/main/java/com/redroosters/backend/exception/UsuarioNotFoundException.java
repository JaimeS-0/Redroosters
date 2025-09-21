package com.redroosters.backend.exception;

// Se lanza cuando no se encontro el usuario

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(String nombre, String email) {
        super("No se encontro el usuario " + nombre + " y email " + email);
    }

    public UsuarioNotFoundException(String email) {
        super(String.format("No se encontró el usuario con email '%s'", email));
    }

}
