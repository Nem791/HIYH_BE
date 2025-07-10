package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileUpdateDto {
    private Integer age;
    private String gender;
    private String race;
    private List<String> chronicDisease;
    private Double weight;
    private Double height;
}