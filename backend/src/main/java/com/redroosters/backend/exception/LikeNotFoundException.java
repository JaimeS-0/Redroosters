package com.redroosters.backend.exception;

public class LikeNotFoundException extends RuntimeException {

    public LikeNotFoundException(String titulo) {
        super("No existe " + titulo + " No se puedo eliminar");
    }
}
