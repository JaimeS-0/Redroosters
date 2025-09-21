package com.redroosters.backend.exception;

// Se lanza cuando no se encontro un album

public class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException(Long id) {
        super("No se encontró el album " + "con el id " + id);
    }

    public AlbumNotFoundException(String titulo) {
        super("No se encontró el album: " + titulo);
    }
}
