package com.example.demo.controllers;

import com.example.demo.dto.BiomarkerFormDto;
import com.example.demo.dto.response.LabInterpretationResponseDto;
import com.example.demo.exceptions.InvalidFormException;
import com.example.demo.services.LabInterpretationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/lab-interpretations")
public class LabInterpretationController {
    private final LabInterpretationService labInterpretationService;

    public LabInterpretationController(LabInterpretationService labInterpretationService) {
        this.labInterpretationService = labInterpretationService;
    }

    @PostMapping
    public ResponseEntity<LabInterpretationResponseDto> createLabInterpretation(
            @RequestParam("file") MultipartFile file,
            @Valid @ModelAttribute BiomarkerFormDto biomarkerData,
            BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            throw new InvalidFormException(bindingResult);
        }

        LabInterpretationResponseDto labInterpretation =
                labInterpretationService.createLabInterpretation(file, biomarkerData);

        return ResponseEntity.ok(labInterpretation);
    }
}
