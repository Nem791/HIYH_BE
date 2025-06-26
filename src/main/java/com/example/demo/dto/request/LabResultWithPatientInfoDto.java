package com.example.demo.dto.request;

import com.example.demo.models.BiomarkerRecord;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LabResultWithPatientInfoDto {

    private List<BiomarkerRecord> labResultData;
    private PatientInfoDto patientInfo;

}