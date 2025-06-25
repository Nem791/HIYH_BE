package com.example.demo.dto.request;

import java.util.List;

public class PatientInfoDto {
    private int age;
    private String gender;
    private String race;
    private List<String> chronicDisease;
    private double weight;
    private double height;

    // getters and setters

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public List<String> getChronicDisease() {
        return chronicDisease;
    }

    public void setChronicDisease(List<String> chronicDisease) {
        this.chronicDisease = chronicDisease;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}