package com.example.demo.services;

import com.example.demo.dto.GptRequest;
import com.example.demo.dto.GptResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LabInterpretationService {
    private final AzureOpenAiService azureOpenAiService;
    private final ObjectMapper objectMapper;

    public LabInterpretationService(AzureOpenAiService azureOpenAiService, ObjectMapper objectMapper) {
        this.azureOpenAiService = azureOpenAiService;
        this.objectMapper = objectMapper;
    }

    public GptResponse createLabInterpretation(GptRequest request) {

        String rawGptResponse = azureOpenAiService.fetchGptEndpoint(request);
        try {
            // Happy-path: deserialize into your Java class
            return objectMapper.readValue(rawGptResponse, GptResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse response as GptResponse", e);
        }
    }
}
