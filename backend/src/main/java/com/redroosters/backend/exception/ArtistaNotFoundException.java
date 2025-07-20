package com.redroosters.backend.exception;

public class ArtistaNotFoundException extends RuntimeException {

    public ArtistaNotFoundException(Long id) {
        super("Artista con ID " + id + " no encontradó");
    }

}
