package com.redroosters.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    // Maneja errores cuando no se encuentra un Album
    @ExceptionHandler(AlbumNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleAlbumNotFound(AlbumNotFoundException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setTitle("Album no encontrado");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja errores cuando no se encuentra un Artista
    @ExceptionHandler(ArtistaNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleArtistaNotFound(ArtistaNotFoundException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setTitle("Artista no encontrado");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja errores cuando no se encuentra una Cancion
    @ExceptionHandler(CancionNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCancionNotFound(CancionNotFoundException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setTitle("Cancion no encontrada");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja errores cuando ya existe el Like
    @ExceptionHandler(LikeAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> LikeAlreadyExistsException(LikeAlreadyExistsException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        error.setTitle("Ya has dado like a la cancion");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // Maneja errores cuando no se encuentra el Like
    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleLikeNotFound(LikeNotFoundException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setTitle("Like no encontrado");
        error.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja errores cuando no se encuentra un Mensaje de Contacto
    @ExceptionHandler(MensajeContactoNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleMensajeContactoNotFound(MensajeContactoNotFoundException ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setTitle("Mensaje de contacto no encontrado");
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

    // Maneja errores cuando ya existe el mismo email
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEmailExists(EmailAlreadyExistsException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Email duplicado");
        problem.setDetail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    // Maneja errores 400 de validacion
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Datos inválidos");
        pd.setDetail("Los datos enviados no son validos");

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        pd.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(pd);
    }

    // 401 - credenciales incorrectas o usuario no encontrado
    @ExceptionHandler({ BadCredentialsException.class, UsernameNotFoundException.class })
    public ResponseEntity<ProblemDetail> handleAuthErrors(RuntimeException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        pd.setTitle("Credenciales inválidas");
        pd.setDetail("Email o contraseña incorrectos.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(pd);
    }

    // 403 - autenticado pero sin permisos
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        pd.setTitle("Acceso denegado");
        pd.setDetail("No tienes permisos para acceder a este recurso.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(pd);
    }

    // 500 - fallo del servidor
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setTitle("Error interno");
        error.setDetail("Se ha producido un error inesperado.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
