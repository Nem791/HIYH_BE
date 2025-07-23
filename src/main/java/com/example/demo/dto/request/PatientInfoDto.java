package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
public class PatientInfoDto {
    private Date dob;
    private String sexAtBirth;
    private String race;
    private List<String> chronicDisease;
    private Double weight;
    private String weightUnit;
    private Double height;
    private String heightUnit;
}