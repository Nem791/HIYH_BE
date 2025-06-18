package com.example.demo.exceptions;

public class PdfProcessingException extends RuntimeException {
    public PdfProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
    public PdfProcessingException(String message) {
        super(message);
    }
}