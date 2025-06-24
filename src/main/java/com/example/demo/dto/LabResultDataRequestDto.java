package com.example.demo.dto;

import com.example.demo.models.BiomarkerValue;

import java.util.Date;
import java.util.Map;

public class LabResultDataRequestDto {
    private Date createdAt;
    private Map<String, BiomarkerValue> biomarkers;

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Map<String, BiomarkerValue> getBiomarkers() { return biomarkers; }
    public void setBiomarkers(Map<String, BiomarkerValue> biomarkers) { this.biomarkers = biomarkers; }
}