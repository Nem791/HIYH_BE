package com.example.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Schema(description = "Response DTO for user profile info (non-sensitive)")
public class UserResponseDto {

    @Schema(example = "abc123-uuid")
    private String id;

    @Schema(example = "John Doe")
    private String fullName;

    @Schema(example = "true")
    private Boolean consented;

    @Schema(example = "John")
    private String preferredName;

    @Schema(example = "Canada")
    private String regionOfResidence;

    @Schema(example = "1990-01-01")
    private Date dob;

    @Schema(example = "Male")
    private String sexAtBirth;

    @Schema(example = "Asian")
    private String race;

    @Schema(example = "[\"Diabetes\", \"Hypertension\"]")
    private List<String> chronicDisease;

    @Schema(example = "70.5")
    private Double weight;

    @Schema(example = "kg")
    private String weightUnit;

    @Schema(example = "175.0")
    private Double height;

    @Schema(example = "cm")
    private String heightUnit;

}