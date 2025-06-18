package com.example.demo.repository;

import com.example.demo.models.BiomarkerRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiomarkerRecordRepository extends MongoRepository<BiomarkerRecord, String> {

}