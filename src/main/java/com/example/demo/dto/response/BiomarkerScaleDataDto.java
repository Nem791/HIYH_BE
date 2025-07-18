package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BiomarkerScaleDataDto {

    private double yellowLowerBound;
    private double yellowUpperBound;
    private Object currentValue;
    private String unit;
    private String referenceRange;
}