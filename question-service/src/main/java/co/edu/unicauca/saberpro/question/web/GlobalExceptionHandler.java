package co.edu.unicauca.saberpro.question.web;

import co.edu.unicauca.saberpro.question.application.exception.QuestionNotFoundException;
import co.edu.unicauca.saberpro.question.application.exception.QuestionValidationException;
import co.edu.unicauca.saberpro.question.application.exception.ValidationServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QuestionValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(QuestionValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", 400, "error", "Bad Request",
                "message", ex.getMessage(), "errors", ex.getErrors()));
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(QuestionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "status", 404, "error", "Not Found", "errors", List.of(ex.getMessage())));
    }

    @ExceptionHandler(ValidationServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleUnavailable(ValidationServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                "status", 503, "error", "Service Unavailable", "errors", List.of(ex.getMessage())));
    }
}
