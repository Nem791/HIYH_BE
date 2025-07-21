package com.example.demo.exceptions;

import com.example.demo.constants.AppConstants;
import com.example.demo.dto.response.ApiErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.Map;
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

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonMappingException(JsonMappingException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                "Invalid user input",
                ex.getOriginalMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<Map<String, String>> handleEmailSendException(EmailSendException ex) {
        Map<String, String> error = Map.of(
                "error", "Email sending failed",
                "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String combinedErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ApiErrorResponse error = new ApiErrorResponse("Validation failed", combinedErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiErrorResponse error = new ApiErrorResponse("Resource Not Found", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Handle missing cookies globally
    @ExceptionHandler(MissingCookieException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingCookie(MissingCookieException ex) {
        String cookieName = ex.getCookieName();

        String message = switch (cookieName) {
            case AppConstants.SIGNUP_TOKEN -> "Signup token is missing. Please verify your email before signing up.";
            case AppConstants.LOGIN_TOKEN -> "Session token is missing. Please log in again.";
            default -> "Required cookie '" + cookieName + "' is missing.";
        };

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                "Missing cookie",
                message
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}