package com.example.demo.repository;

import com.example.demo.models.BiomarkerRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiomarkerRecordRepository extends MongoRepository<BiomarkerRecord, String> {
    List<BiomarkerRecord> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

}