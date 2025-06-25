package com.example.demo.dto.request;

import com.example.demo.models.BiomarkerRecord;

import java.util.List;

public class LabResultWithPatientInfoDto {

    private List<BiomarkerRecord> labResultData;
    private PatientInfoDto patientInfo;

    // getters and setters

    public List<BiomarkerRecord> getLabResultData() {
        return labResultData;
    }

    public void setLabResultData(List<BiomarkerRecord> labResultData) {
        this.labResultData = labResultData;
    }

    public PatientInfoDto getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfoDto patientInfo) {
        this.patientInfo = patientInfo;
    }
}