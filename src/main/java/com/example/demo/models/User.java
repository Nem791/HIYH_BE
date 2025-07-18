package com.example.demo.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    // Optional patient info fields
    private Integer age;
    private String gender;
    private String race;
    private List<String> chronicDisease;
    private Double weight;
    private Double height;
}
