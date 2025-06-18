package com.example.demo.utils;

import com.example.demo.dto.GptMessage;
import com.example.demo.dto.GptRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GptRequestUtils {
    // Fixed system prompt  
    private static final String SYSTEM_PROMPT =
            "You are a medical data extractor. From the raw lab report text, extract only biomarker results. For each biomarker, return a key-value pair where the key is the biomarker name and the value is an object with the following fields: value, resultFlag, referenceRange, resultUnits, and timeResulted. Use null if any field is missing in the report — do not guess or infer. Return the result in strict JSON format only, with no extra explanation or commentary. The value must be a number or string. resultFlag should be one of: Normal, High, Low, Critical, Abnormal, Borderline, or Unknown. timeResulted must be in ISO 8601 format or null. Ignore unrelated text or metadata, but keep in mind if there is a standalone A, that could be the abnormal flag, not part of any biomarkers name, so determine that when you encounter any";

    public static GptRequest biomarkerExtractionRequest(String userContent) {
        ObjectMapper objectMapper = new ObjectMapper();

// Loads from the classpath resource folder
        try (InputStream is = GptRequestUtils.class.getResourceAsStream("/utils/biomarker_response.json")) {
            if (is == null) {
                throw new IllegalArgumentException("File not found: /utils/biomarker_response.json");
            }
            // Deserialize JSON file to Map
            Map<String, Object> responseFormat = objectMapper.readValue(is, new TypeReference<>() {
            });

            // System message
            GptMessage systemMsg = new GptMessage("system", SYSTEM_PROMPT);

            // User message
            GptMessage userMsg = new GptMessage("user", userContent);

            // Build list of messages
            List<GptMessage> messages = Arrays.asList(systemMsg, userMsg);

            // Build and return GptRequest
            GptRequest req = new GptRequest();
            req.setMessages(messages);
            req.setResponse_format(responseFormat);
            return req;
        } catch (Exception e) {
            throw new RuntimeException("Error loading biomarker_response.json", e);
        }
    }
}  