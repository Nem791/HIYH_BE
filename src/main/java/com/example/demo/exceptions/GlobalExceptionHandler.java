package com.example.demo.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle JSON parsing issues (bad response from OpenAI or misconfiguration)
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        ErrorResponse error = new ErrorResponse(
                "Bad response from OpenAI",
                ex.getOriginalMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY); // 502
    }

    // Handle HTTP/API errors when calling OpenAI
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException ex) {
        ErrorResponse error = new ErrorResponse(
                "Failed to connect to OpenAI API",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY); // 502
    }

    // Optional: Catch-all for any unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                "Internal server error",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                "Invalid request",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDbConstraint(DataIntegrityViolationException ex) {
        ErrorResponse error = new ErrorResponse("Database error", ex.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Simple DTO for error response body
        public record ErrorResponse(String error, String details) {
    }
}