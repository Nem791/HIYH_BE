package com.example.demo.exceptions;

import org.springframework.validation.BindingResult;

public class InvalidFormException extends RuntimeException {
    private final BindingResult bindingResult;

    public InvalidFormException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}