package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class BiomarkerFormDto {
    @NotBlank(message = "userId is required")
    private String userId;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}