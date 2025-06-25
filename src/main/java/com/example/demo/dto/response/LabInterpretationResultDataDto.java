package com.example.demo.dto.response;

public class LabInterpretationResultDataDto {

    private String biomarker;
    private BiomarkerScaleDataDto scaleData;
    private String classification;
    private String reason;

    // Getters and setters

    public String getBiomarker() {
        return biomarker;
    }

    public void setBiomarker(String biomarker) {
        this.biomarker = biomarker;
    }

    public BiomarkerScaleDataDto getScaleData() {
        return scaleData;
    }

    public void setScaleData(BiomarkerScaleDataDto scaleData) {
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