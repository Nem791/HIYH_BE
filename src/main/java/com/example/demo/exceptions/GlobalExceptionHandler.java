package com.example.demo.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.stream.Collectors;

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

    @ExceptionHandler(PdfProcessingException.class)
    public ResponseEntity<ErrorResponse> handlePdfProcessing(PdfProcessingException ex) {
        ErrorResponse error = new ErrorResponse("PDF processing error", ex.getMessage());
        // Choose status code appropriate for your app, e.g. 422 Unprocessable Entity
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(GptResponseParseException.class)
    public ResponseEntity<ErrorResponse> handleGptResponseParse(GptResponseParseException ex) {
        ErrorResponse error = new ErrorResponse("GPT response parse error", ex.getMessage());
        // Again, you might choose 502 Bad Gateway or 500 Internal Server Error, as fits
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(InvalidFormException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormException(InvalidFormException ex) {
        String combinedErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse error = new ErrorResponse("Invalid form data", combinedErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 🔐 Authentication & Authorization Exceptions
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ErrorResponse error = new ErrorResponse("Token expired", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex) {
        ErrorResponse error = new ErrorResponse("Invalid JWT token", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse("User not found", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return new ResponseEntity<>(new ErrorResponse("Invalid credentials", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    // Simple DTO for error response body
        public record ErrorResponse(String error, String details) {
    }
}