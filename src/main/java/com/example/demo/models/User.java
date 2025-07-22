package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private String id;

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    private String email;
    private String password; // hashed

    private Boolean consented = false;
    private String verificationCode;
    private Date verificationCodeExpiry;


    // Optional patient info fields
    private String fullName;
    private String preferredName;
    private String regionOfResidence;
    private Date dob;

    private Integer age;
    private String gender;
    private String race;
    private List<String> chronicDisease;
    private Double weight;
    private Double height;

    private Instant createdAt = Instant.now();
}
