package com.example.demo.utils;

import com.example.demo.models.BiomarkerScaleData;
import com.example.demo.models.BiomarkerValue;
import com.example.demo.models.LabInterpretation;
import com.example.demo.models.LabInterpretationResultData;

import java.util.HashMap;
import java.util.Map;

public class LabInterpretationUtils {
    public static void enrichLabInterpretationWithBiomarkerValues(
            LabInterpretation labInterpretation,
            Map<String, BiomarkerValue> biomarkerMap
    ) {
        if (labInterpretation == null
                || labInterpretation.getResultsData() == null
                || biomarkerMap == null) return;

        // Create a normalized map to match keys flexibly
        Map<String, BiomarkerValue> normalizedMap = new HashMap<>();
        for (Map.Entry<String, BiomarkerValue> entry : biomarkerMap.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                String normalizedKey = normalizeKey(entry.getKey());
                normalizedMap.put(normalizedKey, entry.getValue());
            }
        }

        for (LabInterpretationResultData result : labInterpretation.getResultsData()) {
            if (result == null || result.getBiomarker() == null || result.getScaleData() == null) continue;

            String biomarkerKey = normalizeKey(result.getBiomarker());
            BiomarkerValue biomarkerValue = normalizedMap.get(biomarkerKey);

            if (biomarkerValue != null) {
                BiomarkerScaleData scaleData = result.getScaleData();

                scaleData.setCurrentValue(biomarkerValue.getValue());
                scaleData.setUnit(biomarkerValue.getResultUnits());
                scaleData.setReferenceRange(biomarkerValue.getReferenceRange());
            }
        }
    }

    private static String normalizeKey(String input) {
        return input == null ? "" : input.trim().toLowerCase().replaceAll("\\s+", " ");
    }
}
