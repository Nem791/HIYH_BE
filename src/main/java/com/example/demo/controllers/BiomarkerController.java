package com.example.demo.controllers;

import com.example.demo.dto.BiomarkerFormDto;
import com.example.demo.dto.GptRequest;
import com.example.demo.dto.GptResponse;
import com.example.demo.models.BiomarkerRecord;
import com.example.demo.services.BiomarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/biomarkers")
public class BiomarkerController {

    private final BiomarkerService biomarkerService;

    @Autowired
    public BiomarkerController(BiomarkerService biomarkerService) {
        this.biomarkerService = biomarkerService;
    }

    @PostMapping()
    public ResponseEntity<BiomarkerRecord> createBiomarkerRecord(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute BiomarkerFormDto biomarkerData) {
        BiomarkerRecord record = biomarkerService.createBiomarkerRecord(file, biomarkerData);
        return ResponseEntity.status(201).body(record); // HTTP 201 Created, body = created document
    }
}