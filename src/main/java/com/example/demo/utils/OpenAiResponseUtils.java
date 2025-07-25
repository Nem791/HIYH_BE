package com.example.demo.utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class OpenAiResponseUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    // Prevent instantiation
    private OpenAiResponseUtils() {
    }

    public static String extractMessageContentOrEmpty(String apiResponse) {
        try {
            JsonNode root = mapper.readTree(apiResponse);
            JsonNode choices = root.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                JsonNode messageContent = choices.get(0).path("message").path("content");
                if (!messageContent.isMissingNode() && !messageContent.isNull()) {
                    return messageContent.asText();
                }
            }
            // Return empty JSON object if not found
            return "{}";
        } catch (Exception e) {
            // Return empty JSON object on parse/error
            return "{}";
        }
    }
}