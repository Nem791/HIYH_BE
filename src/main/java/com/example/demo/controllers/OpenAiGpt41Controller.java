package com.example.demo.controllers;

import com.example.demo.services.AzureOpenAiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class OpenAiGpt41Controller {
    private final AzureOpenAiService azureOpenAiService;

    public OpenAiGpt41Controller(AzureOpenAiService azureOpenAiService) {
        this.azureOpenAiService = azureOpenAiService;
    }

//    @PostMapping(value = "/gpt-41/chat-completions", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<GptResponse> chatCompletions(@RequestBody GptRequest request) {
//        GptResponse gptResponse = openAiService.fetchChatCompletion(request);
//        return ResponseEntity.ok(gptResponse);
//    }
}