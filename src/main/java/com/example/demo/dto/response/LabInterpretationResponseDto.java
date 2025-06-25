package com.example.demo.dto.response;

import java.time.Instant;
import java.util.List;

public class LabInterpretationResponseDto {

    private String id;
    private String userId;
    private Instant createdAt;
    private String plainLanguageSummary;
    private List<String> recommendations;
    private List<LabInterpretationResultDataDto> resultsData;

    // Getters and setters

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

    public List<LabInterpretationResultDataDto> getResultsData() {
        return resultsData;
    }

    public void setResultsData(List<LabInterpretationResultDataDto> resultsData) {
        this.resultsData = resultsData;
    }
}