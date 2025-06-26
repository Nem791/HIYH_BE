package com.example.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error response body for failed API operations.")
public record ApiErrorResponse(
        @Schema(description = "Short description of the error.", example = "Invalid request")
        String error,
        @Schema(description = "Detailed information about the error.", example = "userId: must not be null")
        String details
) {}