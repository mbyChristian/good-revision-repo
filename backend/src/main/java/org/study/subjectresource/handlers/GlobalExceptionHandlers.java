package org.study.subjectresource.handlers;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.study.subjectresource.entity.Users;
import org.study.subjectresource.exceptions.ObjectValidationException;
import org.study.subjectresource.exceptions.OperationNonPermittedException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Builder
public class GlobalExceptionHandlers {
    @ExceptionHandler(ObjectValidationException.class)
    public ResponseEntity<?> handle(ObjectValidationException e) {
        ExceptionRepresentation representation= ExceptionRepresentation.builder()
                .errorMessage("une exception s'est levee")
                .errorSource(e.getValidationsource())
                .validationErrors(e.getViolations())
                .build();
       return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(representation);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handle(IllegalArgumentException e) {
        ExceptionRepresentation representation= ExceptionRepresentation.builder()
                .errorMessage(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(representation);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handle(EntityNotFoundException e) {
        ExceptionRepresentation representation = new ExceptionRepresentation();
        representation.setErrorMessage(e.getMessage());
        representation.setTimestamp(LocalDateTime.now());
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(representation);
    }
    @ExceptionHandler(OperationNonPermittedException.class)
    public ResponseEntity<?> handle(OperationNonPermittedException e) {
        ExceptionRepresentation representation= ExceptionRepresentation.builder()
                .errorMessage(e.getErrorMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(representation);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handle(DataIntegrityViolationException e) {
        ExceptionRepresentation representation= ExceptionRepresentation.builder()
                .errorMessage(e.getMostSpecificCause().getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(representation);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handle(BadCredentialsException e) {
        ExceptionRepresentation representation= ExceptionRepresentation.builder()
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(representation);

    }
}
