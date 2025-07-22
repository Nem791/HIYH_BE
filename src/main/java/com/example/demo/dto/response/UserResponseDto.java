package com.example.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
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

    @Schema(example = "John")
    private String preferredName;

    @Schema(example = "Canada")
    private String regionOfResidence;

    @Schema(example = "1990-01-01")
    private Date dob;

    @Schema(example = "34")
    private Integer age;

    @Schema(example = "Male")
    private String gender;

    @Schema(example = "Asian")
    private String race;

    @Schema(example = "[\"Diabetes\", \"Hypertension\"]")
    private List<String> chronicDisease;

    @Schema(example = "70.5")
    private Double weight;

    @Schema(example = "175.0")
    private Double height;

    @Schema(example = "2025-01-01T00:00:00Z")
    private Instant createdAt;

}