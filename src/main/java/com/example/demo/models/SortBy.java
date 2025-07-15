package com.example.demo.models;

public enum SortBy {
    REPORTED_ON("reportedOn"),
    ABNORMAL_BIOMARKERS("abnormalBiomarkers"),
    TEST_NAME("testName");

    private final String value;

    SortBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}