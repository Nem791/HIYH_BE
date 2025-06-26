package com.example.demo.exceptions;

import com.example.demo.dto.response.ApiErrorResponse;
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
    public ResponseEntity<ApiErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                "Bad response from OpenAI",
                ex.getOriginalMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY); // 502
    }

    // Handle HTTP/API errors when calling OpenAI
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ApiErrorResponse> handleRestClientException(RestClientException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                "Failed to connect to OpenAI API",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY); // 502
    }

    // Optional: Catch-all for any unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                "Internal server error",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                "Invalid request",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDbConstraint(DataIntegrityViolationException ex) {
        ApiErrorResponse error = new ApiErrorResponse("Database error", ex.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PdfProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handlePdfProcessing(PdfProcessingException ex) {
        ApiErrorResponse error = new ApiErrorResponse("PDF processing error", ex.getMessage());
        // Choose status code appropriate for your app, e.g. 422 Unprocessable Entity
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(GptResponseParseException.class)
    public ResponseEntity<ApiErrorResponse> handleGptResponseParse(GptResponseParseException ex) {
        ApiErrorResponse error = new ApiErrorResponse("GPT response parse error", ex.getMessage());
        // Again, you might choose 502 Bad Gateway or 500 Internal Server Error, as fits
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(InvalidFormException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidFormException(InvalidFormException ex) {
        String combinedErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ApiErrorResponse error = new ApiErrorResponse("Invalid form data", combinedErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 🔐 Authentication & Authorization Exceptions
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ApiErrorResponse error = new ApiErrorResponse("Token expired", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> handleJwtException(JwtException ex) {
        ApiErrorResponse error = new ApiErrorResponse("Invalid JWT token", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(new ApiErrorResponse("User not found", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return new ResponseEntity<>(new ApiErrorResponse("Invalid credentials", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}