package com.example.demo.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class BiomarkerValue {
    private String name;
    private Object value;
    private String resultFlag;
    private String referenceRange;
    private String resultUnits;
    private Date timeResulted;

}