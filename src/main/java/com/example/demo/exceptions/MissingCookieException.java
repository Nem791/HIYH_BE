package com.example.demo.exceptions;

import lombok.Getter;

@Getter
public class MissingCookieException extends RuntimeException {
    private final String cookieName;

    public MissingCookieException(String cookieName) {
        super("Required cookie '" + cookieName + "' is missing");
        this.cookieName = cookieName;
    }

}
