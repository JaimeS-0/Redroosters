package com.redroosters.backend.exception;

public class ArtistaNotFoundException extends RuntimeException {

    public ArtistaNotFoundException(Long id) {
        super("Artista con ID " + id + " no encontrado");
    }

    public ArtistaNotFoundException(String titulo) {
        super("El Artista " + titulo + " no encontrado");
    }

}
