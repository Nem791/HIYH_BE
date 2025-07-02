package com.example.demo.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class BiomarkerScaleData {

    @NotNull
    private double yellowLowerBound;

    @NotNull
    private double yellowUpperBound;

    @NotNull
    private Object currentValue;

    @NotNull
    private String unit;

    @NotNull
    private String referenceRange;

}