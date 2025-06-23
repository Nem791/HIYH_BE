package com.example.demo.utils;

import com.example.demo.constants.AppConstants;
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
            GptRequest req = getGptRequest(userContent, responseFormat);
            return req;
        } catch (Exception e) {
            throw new RuntimeException("Error loading biomarker_response.json", e);
        }
    }

    private static GptRequest getGptRequest(String userContent, Map<String, Object> responseFormat) {
        GptMessage systemMsg = new GptMessage(AppConstants.ROLE_SYSTEM, AppConstants.LAB_INTERPRETATION_SYSTEM_PROMPT);

        // User message
        GptMessage userMsg = new GptMessage(AppConstants.ROLE_USER, userContent);

        // Build list of messages
        List<GptMessage> messages = Arrays.asList(systemMsg, userMsg);

        // Build and return GptRequest
        GptRequest req = new GptRequest();
        req.setMessages(messages);
        req.setResponse_format(responseFormat);
        return req;
    }
}