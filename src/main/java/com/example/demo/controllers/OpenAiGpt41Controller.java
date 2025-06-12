package com.example.demo.controllers;

import com.example.demo.dto.GptRequest;
import com.example.demo.dto.GptResponse;
import com.example.demo.services.OpenAiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class OpenAiGpt41Controller {
    private final OpenAiService openAiService;

    public OpenAiGpt41Controller(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping(value = "/gpt-41/chat-completions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GptResponse> chatCompletions(@RequestBody GptRequest request) {
        String aiResponse = openAiService.fetchChatCompletion(request);


        ObjectMapper mapper = new ObjectMapper();
        GptResponse gptResponse;
        try {
            gptResponse = mapper.readValue(aiResponse, GptResponse.class);
        } catch (Exception e) {
            // Optionally log error here
            gptResponse = new GptResponse(); // or return error/empty
        }

        return ResponseEntity.ok(gptResponse);
    }
}