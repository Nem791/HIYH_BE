package com.example.demo.controllers;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.request.UserRegistrationDto;
import com.example.demo.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Authentication",
        description = "Endpoints for user login, logout, signup, and session verification"
)
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "User login",
            description = "Authenticate user and set JWT httpOnly cookie on successful login.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful, JWT cookie set"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        String jwt = authService.login(request);
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Only set true in production for HTTPS!
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        return ResponseEntity.ok().body(Map.of("message", "Login successful"));
    }

    @Operation(
            summary = "User logout",
            description = "Clears the JWT cookie to log the user out.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logged out successfully")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().body(Map.of("message", "Logged out successfully"));
    }

    @Operation(
            summary = "User signup",
            description = "Register a new user account.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Signup successful"),
                    @ApiResponse(responseCode = "400", description = "Invalid registration data")
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRegistrationDto dto) {
        authService.signup(dto);
        return ResponseEntity.ok(Map.of("message", "Signup successful"));
    }

    @Operation(
            summary = "Get current authenticated user",
            description = "Returns the username/email of the currently authenticated user if logged in.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authenticated user's info"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "details", "You are not logged in."));
        }
        return ResponseEntity.ok(Map.of("user", authentication.getName()));
    }
}