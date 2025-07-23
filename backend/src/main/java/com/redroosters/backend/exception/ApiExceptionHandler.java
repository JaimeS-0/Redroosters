package com.redroosters.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    // Maneja errores cuando no se encuentra un Artista
    @ExceptionHandler(ArtistaNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleArtistaNotFound(ArtistaNotFoundException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setTitle("Artista no encontrado");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja errores cuando no se encuentra un Album
    @ExceptionHandler(AlbumNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleAlbumNotFound(AlbumNotFoundException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setTitle("Album no encontrado");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja errores cuando no se encuentra una Cancion
    @ExceptionHandler(CancionNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCancionNotFound(CancionNotFoundException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setTitle("Cancion no encontrado");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


}
