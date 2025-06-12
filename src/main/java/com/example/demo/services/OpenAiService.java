package com.example.demo.services;

import com.example.demo.dto.GptRequest;
import com.example.demo.utils.OpenAiResponseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService {
    @Value("${openai.api.url}")
    private String openAiApiUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchChatCompletion(GptRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", openAiApiKey);

        HttpEntity<GptRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(openAiApiUrl, entity, String.class);
        String responseBody = response.getBody();

        return OpenAiResponseUtils.extractMessageContentOrEmpty(responseBody);
    }
}