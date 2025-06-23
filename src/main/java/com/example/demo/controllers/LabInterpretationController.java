package com.example.demo.controllers;

import com.example.demo.dto.GptRequest;
import com.example.demo.dto.GptResponse;
import com.example.demo.services.LabInterpretationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lab-interpretations")
public class LabInterpretationController {
    private final LabInterpretationService labInterpretationService;

    public LabInterpretationController(LabInterpretationService labInterpretationService) {
        this.labInterpretationService = labInterpretationService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GptResponse> createLabInterpretation(@RequestBody GptRequest request) {
        GptResponse gptResponse = labInterpretationService.createLabInterpretation(request);
        return ResponseEntity.ok(gptResponse);
    }
}
