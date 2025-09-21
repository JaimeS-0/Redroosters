package com.redroosters.backend.exception;

// Se lanza cuando ya le diste like a la cancion

public class LikeAlreadyExistsException extends RuntimeException {

    public LikeAlreadyExistsException() {
        super("Ya le diste like a esta cancion");
    }
}
