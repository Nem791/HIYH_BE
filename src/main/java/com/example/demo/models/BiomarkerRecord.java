package com.example.demo.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;


@Document(collection = "biomarker_records")
public class BiomarkerRecord {
    @Id
    private String id;

    @CreatedDate
    private Date createdAt;

    private Date reportedOn;

    private String userId;
    private Map<String, BiomarkerValue> biomarkers;

    public String getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, BiomarkerValue> getBiomarkers() {
        return biomarkers;
    }

    public Date getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(Date reportedOn) {
        this.reportedOn = reportedOn;
    }

    public void setBiomarkers(Map<String, BiomarkerValue> biomarkers) {
        this.biomarkers = biomarkers;
    }
}