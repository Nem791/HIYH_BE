package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "pending_users")
public class PendingUser {

    @Id
    private String id;

    private String email;

    private String verificationCode; // Store plaintext or hash (see notes below)

    private Instant verificationCodeExpiry;

    private Instant createdAt;

    public PendingUser() {
        this.id = UUID.randomUUID().toString();
    }

    public PendingUser(String email, String verificationCode, Instant verificationCodeExpiry) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.verificationCode = verificationCode;
        this.verificationCodeExpiry = verificationCodeExpiry;
        this.createdAt = Instant.now();
    }
}