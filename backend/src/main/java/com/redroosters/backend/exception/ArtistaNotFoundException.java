package com.redroosters.backend.exception;

// Se lanza cuando no se encontro un Artista

public class ArtistaNotFoundException extends RuntimeException {

    public ArtistaNotFoundException(Long id) {
        super("Artista con ID " + id + " no encontrado");
    }

    public ArtistaNotFoundException(String titulo) {
        super("El Artista " + titulo + " no encontrado");
    }

}
