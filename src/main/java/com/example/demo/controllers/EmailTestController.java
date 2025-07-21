package com.example.demo.controllers;

import com.example.demo.services.VerificationEmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-email")
public class EmailTestController {

    private final VerificationEmailService emailService;

    public EmailTestController(VerificationEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public String sendTestEmail(@RequestParam String email) throws Exception {
        // Generate a dummy token for testing
        String token = java.util.UUID.randomUUID().toString();

        emailService.sendVerificationEmail(email, token);

        return "Verification email sent to " + email;
    }
}