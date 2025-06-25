package com.example.demo.repository;

import com.example.demo.models.LabInterpretation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabInterpretationRepository extends MongoRepository<LabInterpretation, String> {

    // Custom finder method to get all lab interpretations for a user
    List<LabInterpretation> findByUserId(String userId);
}