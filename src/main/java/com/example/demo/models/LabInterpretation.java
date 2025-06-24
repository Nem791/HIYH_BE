package com.example.demo.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "lab_interpretations")
public class LabInterpretation {

    @Id
    private String id;

    @NotNull
    private String userId;

    @CreatedDate
    private Instant createdAt;

    @NotEmpty
    private String plainLanguageSummary;

    @NotEmpty
    private List<String> recommendations;

    @NotEmpty
    private List<LabInterpretationResultData> resultsData;

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getPlainLanguageSummary() {
        return plainLanguageSummary;
    }

    public void setPlainLanguageSummary(String plainLanguageSummary) {
        this.plainLanguageSummary = plainLanguageSummary;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public List<LabInterpretationResultData> getResultsData() {
        return resultsData;
    }

    public void setResultsData(List<LabInterpretationResultData> resultsData) {
        this.resultsData = resultsData;
    }
}
