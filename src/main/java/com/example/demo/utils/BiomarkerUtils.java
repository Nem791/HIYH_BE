package com.example.demo.utils;

import com.example.demo.models.BiomarkerRecord;
import com.example.demo.models.BiomarkerValue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BiomarkerUtils {
    public static Map<String, BiomarkerValue> mergeBiomarkersWithPriority(List<BiomarkerRecord> records) {
        Map<String, BiomarkerValue> merged = new LinkedHashMap<>();

        for (BiomarkerRecord record : records) {
            if (record.getBiomarkers() != null) {
                for (Map.Entry<String, BiomarkerValue> entry : record.getBiomarkers().entrySet()) {
                    // Only put if key does not already exist
                    merged.putIfAbsent(entry.getKey(), entry.getValue());
                }
            }
        }

        return merged;
    }
}
