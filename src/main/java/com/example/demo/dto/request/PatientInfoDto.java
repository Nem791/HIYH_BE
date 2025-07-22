package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class PatientInfoDto {
    private String fullName;
    private String preferredName;
    private String regionOfResidence;
    private Date dob;
    private String sexAtBirth;
    private String race;
    private List<String> chronicDisease;
    private Double weight;
    private Double height;
    private Boolean consented;
}