package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;


@Getter
@Document(collection = "biomarker_records")
public class BiomarkerRecord {
    @Id
    private String id;

    @CreatedDate
    private Date createdAt;

    @Setter
    private Date reportedOn;

    @Setter
    private String userId;

    @Setter
    private Map<String, BiomarkerValue> biomarkers;

}