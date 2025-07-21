package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResendRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

}
