package com.example.demo.controllers;

import com.example.demo.dto.BiomarkerFormDto;
import com.example.demo.services.BiomarkerService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private static final Logger log = LoggerFactory.getLogger(PdfController.class);
    private final BiomarkerService biomarkerService;

    @Autowired
    public PdfController(BiomarkerService biomarkerService) {
        this.biomarkerService = biomarkerService;
    }
}