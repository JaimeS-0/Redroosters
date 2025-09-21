package com.redroosters.backend.exception;

// Se lanza cuando no se encontro un mensaje de contacto

public class MensajeContactoNotFoundException extends RuntimeException {
    public MensajeContactoNotFoundException(Long id) {
        super("No se encontró el mensaje de contacto con id=" + id);
    }
}
