package com.redroosters.backend.exception;

public class AlbumNotFoundException extends RuntimeException {

    public AlbumNotFoundException(Long id) {
        super("No se encontró el album: " + " y con el id " + id);
    }

    public AlbumNotFoundException(String titulo) {
        super("No se encontró el album: " + titulo);
    }
}
