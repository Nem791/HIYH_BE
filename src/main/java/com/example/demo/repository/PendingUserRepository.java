package com.example.demo.repository;

import com.example.demo.models.PendingUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PendingUserRepository extends MongoRepository<PendingUser, String> {
    Optional<PendingUser> findByEmail(String email);
    void deleteByEmail(String email);
}