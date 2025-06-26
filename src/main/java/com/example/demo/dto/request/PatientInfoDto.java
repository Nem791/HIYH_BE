package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PatientInfoDto {
    private int age;
    private String gender;
    private String race;
    private List<String> chronicDisease;
    private double weight;
    private double height;

}