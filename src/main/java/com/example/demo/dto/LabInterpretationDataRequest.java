package com.example.demo.dto;

import java.util.List;

public class LabInterpretationDataRequest {
    private List<BiomarkerRecordDto> labResultData;
    private PatientInfoDto patientInfo;

    public List<BiomarkerRecordDto> getLabResultData() {
        return labResultData;
    }

    public void setLabResultData(List<BiomarkerRecordDto> labResultData) {
        this.labResultData = labResultData;
    }

    public PatientInfoDto getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfoDto patientInfo) {
        this.patientInfo = patientInfo;
    }
}