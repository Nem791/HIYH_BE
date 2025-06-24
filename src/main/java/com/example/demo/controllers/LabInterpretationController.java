package com.example.demo.controllers;

import com.example.demo.dto.BiomarkerFormDto;
import com.example.demo.dto.GptRequest;
import com.example.demo.dto.GptResponse;
import com.example.demo.models.BiomarkerRecord;
import com.example.demo.services.LabInterpretationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/lab-interpretations")
public class LabInterpretationController {
    private final LabInterpretationService labInterpretationService;

    public LabInterpretationController(LabInterpretationService labInterpretationService) {
        this.labInterpretationService = labInterpretationService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GptResponse> createLabInterpretation(@RequestBody GptRequest request) {
    }

    @PostMapping()
    public ResponseEntity<BiomarkerRecord> createBiomarkerRecord(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute BiomarkerFormDto biomarkerData) {
        BiomarkerRecord record = biomarkerService.createBiomarkerRecord(file, biomarkerData);

        GptResponse gptResponse = labInterpretationService.createLabInterpretation(request);
        return ResponseEntity.ok(gptResponse);
    }
}
