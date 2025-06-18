package com.example.demo.exceptions;

public class GptResponseParseException extends RuntimeException {
    public GptResponseParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public GptResponseParseException(String message) {
        super(message);
    }
}