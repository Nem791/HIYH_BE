package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.models.BiomarkerRecord;
import com.example.demo.services.BiomarkerService;
import com.example.demo.services.LabInterpretationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/biomarkers")
public class BiomarkerController {
    private final BiomarkerService biomarkerService;
    private final LabInterpretationService labInterpretationService;

    @Autowired
    public BiomarkerController(BiomarkerService biomarkerService, LabInterpretationService labInterpretationService) {
        this.biomarkerService = biomarkerService;
        this.labInterpretationService = labInterpretationService;
    }
}