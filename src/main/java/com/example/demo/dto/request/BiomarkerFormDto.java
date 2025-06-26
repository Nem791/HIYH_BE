package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BiomarkerFormDto {
    @NotBlank(message = "userId is required")
    private String userId;

}