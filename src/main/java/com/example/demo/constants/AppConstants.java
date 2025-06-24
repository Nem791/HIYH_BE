package com.example.demo.constants;

public final class AppConstants {
    private AppConstants() {} // Prevents instantiation

    public static final String APP_NAME = "My Spring Boot App";
    public static final int MAX_USERS = 100;

    // Grouped by theme
    public static final String ROLE_USER = "user";
    public static final String ROLE_SYSTEM = "system";
    public static final String LAB_INTERPRETATION_SYSTEM_PROMPT =
            "You are a doctor analyzing lab results. Use the current result, patient info, and timeline to provide a 3–4 sentence summary and bullet point recommendations with next steps addressed to the patient, make sure to include lifestyle recommendations. Order recommendations in order of importance. Also provide an RYG (red/yellow/green) alert values (2 decimals). Red = critical, yellow = warning, green = within the reference range (reference_range.low to high). The RYG scale wraps around the green range: red → yellow → green → yellow → red. yellow_lower_bound is the red/yellow cutoff below the green range; yellow_upper_bound is the red/yellow cutoff above. Yellow must not overlap green. Base thresholds on patient history, context, and literature (not fixed %). If near the green range (within 5%) or yellow is normal for the patient, classify as green/yellow with ≤2-sentence reasoning. Use a friendly greeting then professional and data backed tone after, 9th-grade language, no diagnoses or medication advice. Summary should include important numbers (values outside reference range)";
    public static final String BIOMARKER_EXTRACTION_SYSTEM_PROMPT =
            "You are a medical data extractor. From the raw lab report text, extract only biomarker results. For each biomarker, return a key-value pair where the key is the biomarker name and the value is an object with the following fields: value, resultFlag, referenceRange, resultUnits, and timeResulted. Use null if any field is missing in the report — do not guess or infer. Return the result in strict JSON format only, with no extra explanation or commentary. The value must be a number or string. resultFlag should be one of: Normal, High, Low, Critical, Abnormal, Borderline, or Unknown. timeResulted must be in ISO 8601 format or null. Ignore unrelated text or metadata, but keep in mind if there is a standalone A, that could be the abnormal flag, not part of any biomarkers name, so determine that when you encounter any";

}