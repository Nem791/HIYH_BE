package com.example.demo.dto;

import com.example.demo.models.BiomarkerValue;

import java.util.Date;
import java.util.Map;

public class BiomarkerRecordDto {
    private String id;
    private Date createdAt;
    private String userId;
    private Map<String, BiomarkerValue> biomarkers;

    public BiomarkerRecordDto(Date createdAt, String userId, Map<String, BiomarkerValue> biomarkers) {

        this.createdAt = createdAt;
        this.userId = userId;
        this.biomarkers = biomarkers;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, BiomarkerValue> getBiomarkers() {
        return biomarkers;
    }
}
