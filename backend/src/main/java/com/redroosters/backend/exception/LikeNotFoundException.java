package com.redroosters.backend.exception;

// Se lanza cuando no se encontro el likeen una cancion

public class LikeNotFoundException extends RuntimeException {

    public LikeNotFoundException(String titulo) {
        super("No existe " + titulo + " No se puedo eliminar");
    }
}
