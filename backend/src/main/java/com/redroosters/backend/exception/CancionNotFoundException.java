package com.redroosters.backend.exception;

public class CancionNotFoundException extends RuntimeException {

    public CancionNotFoundException(Long id) {
        super("Cancion con ID " + id + " no encontrado");
    }

    public CancionNotFoundException(String titulo) {
        super("La Cancion " + titulo + " no encontrado");
    }
}
