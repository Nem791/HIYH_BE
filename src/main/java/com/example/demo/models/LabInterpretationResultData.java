package com.example.demo.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LabInterpretationResultData {

    @NotNull
    private String biomarker;

    @NotNull
    private BiomarkerScaleData scaleData;

    @NotNull
    private String classification;

    @NotNull
    private String reason;

}