package com.example.demo.controllers;

import com.example.demo.models.BiomarkerRecord;
import com.example.demo.services.BiomarkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biomarkers")
@Tag(name = "Biomarkers/ Lab Test Results", description = "Endpoints for querying biomarker records/ lab test results")
public class BiomarkerController {

    private final BiomarkerService biomarkerService;

    @Autowired
    public BiomarkerController(BiomarkerService biomarkerService) {
        this.biomarkerService = biomarkerService;
    }

    @GetMapping()
    @Operation(
            summary = "Get list of recent biomarker records (Lab test results)",
            description = "Fetches the most recent biomarker records for a specific user with pagination support"
    )
    public Page<BiomarkerRecord> getLatestBiomarkers(
            @Parameter(description = "User ID to filter biomarker records", example = "abc123", required = true)
            @RequestParam String userId,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size (number of records per page)", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return biomarkerService.getLatestBiomarkerRecords(userId, page, size);
    }
}