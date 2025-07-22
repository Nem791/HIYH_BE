package com.example.demo.controllers;

import com.example.demo.dto.request.UserProfileUpdateDto;
import com.example.demo.dto.response.ApiErrorResponse;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.filter.CustomUserDetails;
import com.example.demo.services.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for user profile management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/me")
    @Operation(
            summary = "Update current user's profile",
            description = "Updates the profile information of the authenticated user. Only non-null fields will be updated."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or mapping error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized (missing or invalid JWT)",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<?> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserProfileUpdateDto updateDto
    ) throws JsonMappingException {
        userService.updateUserProfile(userDetails.getId(), updateDto);
        return ResponseEntity.ok().body(
                java.util.Map.of("message", "Profile updated successfully")
        );
    }

    @GetMapping("/profile-info")
    @Operation(
            summary = "Get current user's profile",
            description = "Fetches the profile information of the authenticated user (non-sensitive data)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully fetched user profile",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized (missing or invalid JWT)",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<UserResponseDto> getProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserResponseDto userDto = userService.getUserProfile(userDetails.getId());
        return ResponseEntity.ok(userDto);
    }

}