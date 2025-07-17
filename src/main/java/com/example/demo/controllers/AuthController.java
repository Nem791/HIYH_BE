package com.example.demo.controllers;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.request.ResendRequest;
import com.example.demo.dto.request.SendVerificationCodeRequest;
import com.example.demo.dto.request.UserRegistrationDto;
import com.example.demo.dto.request.VerifyEmailCodeRequest;
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
            summary = "Pre-signup: request verification code",
            description = "Start sign-up process by requesting a verification code to email."
    )
    @PostMapping("/pre-signup")
    public ResponseEntity<?> preSignup(@RequestBody SendVerificationCodeRequest request) {
        authService.preSignup(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Verification code sent to email"));
    }

    @Operation(
            summary = "Verify email code",
            description = "Verify the 6-digit code and set sign-up token as HttpOnly cookie.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email verified, sign-up token set"),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired code"),
                    @ApiResponse(responseCode = "404", description = "Pending user not found")
            }
    )
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(
            @RequestParam String email,
            @RequestParam String code,
            HttpServletResponse response) {

        String token = authService.verifyEmail(email, code);
        System.out.println(token);
        long SIGN_UP_TOKEN_EXPIRY_MS = 15 * 60 * 1000;

        Cookie cookie = new Cookie("signup_token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (SIGN_UP_TOKEN_EXPIRY_MS / 1000));
        response.addCookie(cookie);

        return ResponseEntity.ok("Verification successful");
    }

    @Operation(
            summary = "User signup (complete account creation)",
            description = "Create user account using verified email (requires sign-up token in HttpOnly cookie).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Signup successful"),
                    @ApiResponse(responseCode = "400", description = "Invalid signup token or email mismatch")
            }
    )
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRegistrationDto dto,
                                    @CookieValue("signup_token") String signupToken) {
        authService.signup(dto, signupToken);
        return ResponseEntity.ok(Map.of("message", "Signup successful"));
    }

    @Operation(
            summary = "Resend verification code",
            description = "Resend the verification code for pending sign-up. Can be rate-limited client-side."
    )
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody ResendRequest request) {
        authService.preSignup(request.getEmail()); // Use same logic as pre-signup for resends
        return ResponseEntity.ok(Map.of("message", "Verification code resent successfully!"));
    }

    @Operation(
            summary = "User login",
            description = "Authenticate user and set JWT httpOnly cookie on successful login."
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        String jwt = authService.login(request);
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set true in production for HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Login successful"));
    }

    @Operation(
            summary = "User logout",
            description = "Clears the JWT cookie to log the user out."
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @Operation(
            summary = "Get current authenticated user",
            description = "Returns the currently authenticated user's info."
    )
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "details", "You are not logged in."));
        }
        return ResponseEntity.ok(Map.of("user", authentication));
    }
}
