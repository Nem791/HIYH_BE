package com.example.demo.dto;

import java.util.List;

public class PatientInfoDto {
    private int age;
    private String gender;
    private String race;
    private List<String> chronicDisease;
    private int weight;
    private int height;

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }

    public List<String> getChronicDisease() { return chronicDisease; }
    public void setChronicDisease(List<String> chronicDisease) { this.chronicDisease = chronicDisease; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
}