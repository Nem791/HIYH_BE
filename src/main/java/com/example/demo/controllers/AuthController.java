package com.example.demo.controllers;

import com.example.demo.constants.AppConstants;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.request.ResendRequest;
import com.example.demo.dto.request.UserRegistrationDto;
import com.example.demo.dto.request.VerifyEmailCodeRequest;
import com.example.demo.services.AuthService;
import com.example.demo.utils.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> preSignup(@Valid @RequestBody ResendRequest request) {
        authService.preSignup(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Verification code sent to email"));
    }

    @Operation(
            summary = "Resend verification code",
            description = "Resend the verification code for pending sign-up. Can be rate-limited client-side."
    )
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@Valid @RequestBody ResendRequest request) {
        authService.preSignup(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Verification code resent successfully!"));
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
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailCodeRequest request,
                                         HttpServletResponse response) {

        String token = authService.verifyEmail(request.getEmail(), request.getCode());
        response.addCookie(
                CookieUtil.createHttpOnlyCookie(AppConstants.SIGNUP_TOKEN, token, AppConstants.SIGNUP_TOKEN_EXPIRY_1_MIN_MS)
        );

        return ResponseEntity.ok(Map.of("message", "Verification successful"));
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
                                    @CookieValue(value = AppConstants.SIGNUP_TOKEN, required = false) String signupToken,
                                    HttpServletResponse response) {
        authService.signup(dto, signupToken);
        response.addCookie(CookieUtil.clearCookie(AppConstants.SIGNUP_TOKEN));

        return ResponseEntity.ok(Map.of("message", "Signup successful"));
    }

    @Operation(
            summary = "User login",
            description = "Authenticate user and set JWT httpOnly cookie on successful login."
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        String jwt = authService.login(request);
        response.addCookie(CookieUtil.createHttpOnlyCookie(AppConstants.LOGIN_TOKEN, jwt, AppConstants.JWT_EXPIRY_24_HOURS_MS));

        return ResponseEntity.ok(Map.of("message", "Login successful"));
    }

    @Operation(
            summary = "User logout",
            description = "Clears the JWT cookie to log the user out."
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.addCookie(CookieUtil.clearCookie(AppConstants.LOGIN_TOKEN));

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
