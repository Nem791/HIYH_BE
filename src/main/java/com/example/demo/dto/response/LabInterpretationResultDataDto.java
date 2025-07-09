package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LabInterpretationResultDataDto {

    private String biomarker;
    private BiomarkerScaleDataDto scaleData;
    private String classification;
    private String definition;

}