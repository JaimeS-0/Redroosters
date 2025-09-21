package com.redroosters.backend.exception;

// Se lanza cuando no se encontro una Cancion

public class CancionNotFoundException extends RuntimeException {

    public CancionNotFoundException(Long id) {
        super("Cancion con ID " + id + " no encontrado");
    }

    public CancionNotFoundException(String titulo) {
        super("La Cancion " + titulo + " no encontrado");
    }
}
