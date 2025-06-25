package com.example.demo.dto.response;

public class BiomarkerScaleDataDto {

    private double yellowLowerBound;
    private double yellowUpperBound;

    // Getters and setters

    public double getYellowLowerBound() {
        return yellowLowerBound;
    }

    public void setYellowLowerBound(double yellowLowerBound) {
        this.yellowLowerBound = yellowLowerBound;
    }

    public double getYellowUpperBound() {
        return yellowUpperBound;
    }

    public void setYellowUpperBound(double yellowUpperBound) {
        this.yellowUpperBound = yellowUpperBound;
    }
}