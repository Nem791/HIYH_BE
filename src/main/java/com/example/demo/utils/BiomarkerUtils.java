package com.example.demo.utils;

import com.example.demo.models.BiomarkerRecord;
import com.example.demo.models.BiomarkerValue;

import java.util.HashMap;
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

    public static void sanitizeBiomarkerKeys(Map<String, BiomarkerValue> biomarkers) {
        if (biomarkers == null || biomarkers.isEmpty()) return;

        Map<String, BiomarkerValue> sanitized = new HashMap<>();
        for (Map.Entry<String, BiomarkerValue> entry : biomarkers.entrySet()) {
            String originalKey = entry.getKey();
            String sanitizedKey = sanitizeMongoKey(originalKey);

            BiomarkerValue value = entry.getValue();
            value.setName(originalKey);

            sanitized.put(sanitizedKey, value);
        }

        biomarkers.clear();
        biomarkers.putAll(sanitized);
    }


    private static String sanitizeMongoKey(String key) {
        if (key == null) return null;
        return key
                .replace(".", "·")  // or "_"
                .replace("/", "-")
                .trim();
    }
}
