package com.example.demo.services;

import com.example.demo.dto.GptRequest;
import com.example.demo.utils.OpenAiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AzureOpenAiService {
    private final RestTemplate restTemplate;
    private final String openAiApiKey;
    private final String openAiApiUrl;

    @Autowired
    public AzureOpenAiService(
            RestTemplate restTemplate,
            @Value("${openai.api.key}") String openAiApiKey,
            @Value("${openai.api.url}") String openAiApiUrl
    ) {
        this.restTemplate = restTemplate;
        this.openAiApiKey = openAiApiKey;
        this.openAiApiUrl = openAiApiUrl;
    }

    public String fetchGptEndpoint(GptRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", openAiApiKey);

        HttpEntity<GptRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(openAiApiUrl, entity, String.class);
        System.out.println(response);
        String responseBody = response.getBody();
        return OpenAiResponseUtils.extractMessageContentOrEmpty(responseBody);
    }
}