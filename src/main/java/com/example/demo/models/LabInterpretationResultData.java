package com.example.demo.models;

import jakarta.validation.constraints.NotNull;

public class LabInterpretationResultData {

    @NotNull
    private String biomarker;

    @NotNull
    private BiomarkerScaleData scaleData;

    @NotNull
    private String classification;

    @NotNull
    private String reason;

    // Getters & Setters

    public String getBiomarker() {
        return biomarker;
    }

    public void setBiomarker(String biomarker) {
        this.biomarker = biomarker;
    }

    public BiomarkerScaleData getScaleData() {
        return scaleData;
    }

    public void setScaleData(BiomarkerScaleData scaleData) {
        this.scaleData = scaleData;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}