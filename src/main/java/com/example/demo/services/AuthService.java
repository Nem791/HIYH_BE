package com.example.demo.services;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.request.UserRegistrationDto;
import com.example.demo.exceptions.EmailSendException;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.models.PendingUser;
import com.example.demo.models.User;
import com.example.demo.repository.PendingUserRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PendingUserRepository pendingUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final VerificationEmailService verificationEmailService;

    private final long SIGN_UP_TOKEN_EXPIRY_MS = 15 * 60 * 1000; // 15 minutes

    public AuthService(UserRepository userRepository, PendingUserRepository pendingUserRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, ObjectMapper objectMapper, VerificationEmailService verificationEmailService) {
        this.userRepository = userRepository;
        this.pendingUserRepository = pendingUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.verificationEmailService = verificationEmailService;
    }

    public void preSignup(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already taken");
        }

        String code = generateVerificationCode();
        Instant expiry = Instant.now().plus(Duration.ofMinutes(10));

        PendingUser pendingUser = pendingUserRepository.findByEmail(email.trim().toLowerCase())
                .orElse(new PendingUser(email, code, expiry));

        if (pendingUser.getId() != null) {
            pendingUser.setVerificationCode(code);
            pendingUser.setVerificationCodeExpiry(expiry);
            pendingUser.setCreatedAt(Instant.now());
        }
        pendingUserRepository.save(pendingUser);

        try {
            verificationEmailService.sendVerificationCode(pendingUser.getEmail(), code);
        } catch (Exception e) {
            throw new EmailSendException("Failed to send verification email, please try again", e);
        }
    }


    public String verifyEmail(String email, String code) {
        PendingUser pendingUser = pendingUserRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Pending user not found"));

        if (pendingUser.getVerificationCodeExpiry().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Verification code expired");
        }

        if (!pendingUser.getVerificationCode().equals(code)) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        pendingUser.setVerificationCode(null);
        pendingUser.setVerificationCodeExpiry(Instant.now()); // Optional: mark as expired immediately
        pendingUserRepository.save(pendingUser);

        return jwtUtil.generateSignupToken(email, SIGN_UP_TOKEN_EXPIRY_MS);
    }



    public void signup(UserRegistrationDto dto, String signupTokenFromCookie) {
        // Validate token
        Claims claims = jwtUtil.getAllClaimsFromToken(signupTokenFromCookie);

        String emailFromToken = claims.getSubject();
        if (!dto.getEmail().equalsIgnoreCase(emailFromToken)) {
            throw new IllegalArgumentException("Email does not match verified token");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        User user = objectMapper.convertValue(dto, User.class);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setIsVerified(true); // Optional since signup is verification, but safe to include

        userRepository.save(user);

        // Cleanup PendingUser
        pendingUserRepository.deleteByEmail(dto.getEmail());
    }

    public String login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return jwtUtil.generateToken(user.getEmail());
    }

    public String generateVerificationCode() {
        int code = ThreadLocalRandom.current().nextInt(100000, 999999);
        return String.valueOf(code);
    }

    private void generateAndSetVerificationCode(User user) {
        String code = generateVerificationCode();
        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(Date.from(Instant.now().plus(Duration.ofMinutes(10))));
    }

    public void resendVerificationCode(String email, Boolean isResend) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        generateAndSetVerificationCode(user);

        if (isResend)
            userRepository.save(user);

        verificationEmailService.sendVerificationCode(email, user.getVerificationCode());
    }


}