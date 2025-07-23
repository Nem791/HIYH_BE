package com.example.demo.constants;

import lombok.Getter;

@Getter
public enum TestType {
    BLOOD_TEST("bloodTest"),
    LIPID_PANEL("lipidPanel"),
    URINE_TEST("urineTest"),
    OTHERS("others");

    private final String value;

    TestType(String value) {
        this.value = value;
    }

}