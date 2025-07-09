package com.example.demo.repository;

import com.example.demo.models.LabInterpretation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabInterpretationRepository extends MongoRepository<LabInterpretation, String>, LabInterpretationRepositoryCustom {

    // Custom finder method to get all lab interpretations for a user
    Optional<LabInterpretation> findByIdAndUserId(String id, String userId);
    List<LabInterpretation> findByUserId(String userId);
    Page<LabInterpretation> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
}