package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailVerificationResponse {
    private boolean success;
    private String message;


    public EmailVerificationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}