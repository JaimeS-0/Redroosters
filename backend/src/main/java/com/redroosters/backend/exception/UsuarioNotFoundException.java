package com.redroosters.backend.exception;

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(String nombre) {
        super("No se encontro el usuario " + nombre);
    }
}
