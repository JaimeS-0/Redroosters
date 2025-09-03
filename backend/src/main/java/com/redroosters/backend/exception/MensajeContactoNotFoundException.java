package com.redroosters.backend.exception;

public class MensajeContactoNotFoundException extends RuntimeException {
    public MensajeContactoNotFoundException(Long id) {
        super("No se encontró el mensaje de contacto con id=" + id);
    }
}
