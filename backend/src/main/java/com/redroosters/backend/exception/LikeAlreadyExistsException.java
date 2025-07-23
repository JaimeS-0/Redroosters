package com.redroosters.backend.exception;

public class LikeAlreadyExistsException extends RuntimeException {

    public LikeAlreadyExistsException() {
        super("Ya le diste like a esta cancion");
    }
}
