package com.redroosters.backend.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super("El nombre de usuario '" + username + "' ya existe");
    }
}
