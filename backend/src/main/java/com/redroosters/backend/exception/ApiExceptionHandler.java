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

    // Maneja errores cuando no se encuentra un Usuario
    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setTitle("Usuario no encontrado");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja errores cuando ya existe el Like
    @ExceptionHandler(LikeAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> LikeYaExisteException(LikeAlreadyExistsException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        error.setTitle("Ya has dado like a la cancion");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // Maneja errores cuando ya existe el Like
    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleLikeNotFound(LikeNotFoundException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setTitle("Like no encontrado");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja errores cuando ya existe el mismo nombre de usuario
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleUsernameExists(UsernameAlreadyExistsException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Nombre de usuario duplicado");
        problem.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    // Maneja errores cuando ya existe el mismo email
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEmailExists(EmailAlreadyExistsException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Email duplicado");
        problem.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    // Fallo inesperado del servidor 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setTitle("Error interno");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
}
