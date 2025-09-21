package com.redroosters.backend.exception;

// Se lanza cuando el email ya existe

public class EmailAlreadyExistsException extends RuntimeException {

  public EmailAlreadyExistsException(String email) {
    super("El email '" + email + "' ya está en uso");
  }
}
