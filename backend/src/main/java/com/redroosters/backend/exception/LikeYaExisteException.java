package com.redroosters.backend.exception;

public class LikeYaExisteException extends RuntimeException {

    public LikeYaExisteException() {
        super("Ya le diste like a esta cancion");
    }
}
