package com.redroosters.backend.exception;

public class EmailAlreadyExistsException extends RuntimeException {

  public EmailAlreadyExistsException(String email) {
    super("El email '" + email + "' ya está en uso");
  }
}
