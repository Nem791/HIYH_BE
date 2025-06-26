package com.example.demo.models;

import jakarta.validation.constraints.NotNull;

public class BiomarkerScaleData {

    @NotNull
    private double yellowLowerBound;

    @NotNull
    private double yellowUpperBound;

    // Getters & Setters

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