package com.example.demo.controllers;

import com.example.demo.dto.request.BiomarkerFormDto;
import com.example.demo.dto.response.ApiErrorResponse;
import com.example.demo.dto.response.LabInterpretationResponseDto;
import com.example.demo.dto.response.LabInterpretationRecentListDto;
import com.example.demo.filter.CustomUserDetails;
import com.example.demo.models.LabInterpretation;
import com.example.demo.services.LabInterpretationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.demo.models.SortBy;
import com.example.demo.models.TestType;

@RestController
@RequestMapping("/api/lab-interpretations")
@Tag(name = "Lab Interpretations", description = "Endpoints for read/create lab interpretations")
public class LabInterpretationController {
    private final LabInterpretationService labInterpretationService;

    public LabInterpretationController(LabInterpretationService labInterpretationService) {
        this.labInterpretationService = labInterpretationService;
    }

    @GetMapping("")
    @Operation(
            summary = "Get list of lab interpretations (paginated)",
            description = "Fetches a paginated list of lab interpretations (full lab report analysis) for a specific user, sorted from most recent to oldest."
    )
    public Page<LabInterpretationRecentListDto> getLabInterpretations(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (number of records per page)", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by: 'REPORTED_ON' or 'ABNORMAL_BIOMARKERS' or 'TEST_NAME'", example = "REPORTED_ON")
            @RequestParam(defaultValue = "REPORTED_ON") SortBy sortBy,
            @Parameter(description = "Sort order: 'ASC' or 'DESC'", example = "DESC")
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder,
            @Parameter(description = "Start date for filtering (ISO 8601)", example = "2025-05-20T00:00:00Z")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "End date for filtering (ISO 8601)", example = "2025-05-22T23:59:59Z")
            @RequestParam(required = false) String endDate,
            @Parameter(description = "Only show results with abnormal biomarkers (true/false)", example = "false")
            @RequestParam(defaultValue = "false") boolean onlyAbnormal,
            @Parameter(description = "Filter by test type. Options (based on Figma): 'BLOOD_TEST', 'LIPID_PANEL', 'URINE_TEST', 'OTHERS'", example = "BLOOD_TEST")
            @RequestParam(defaultValue = "BLOOD_TEST") TestType testType
    ) {
        return labInterpretationService.getLabInterpretations(userDetails.getId(), page, size, sortBy, sortOrder, startDate, endDate, onlyAbnormal, testType);
    }


    @Operation(
            summary = "Create a lab interpretation",
            description = "Uploads a lab file and biomarker metadata, returns lab interpretation analysis."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lab interpretation created",
                    content = @Content(schema = @Schema(implementation = LabInterpretationResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or form data",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LabInterpretationResponseDto> createLabInterpretation(
            @RequestPart(value = "file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        LabInterpretationResponseDto response =
                labInterpretationService.createLabInterpretation(file, userDetails.getId());

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@ownershipSecurity.isLabOwner(#id, principal.id)")
    @GetMapping("/{id}")
    @Operation(
            summary = "Get a lab interpretation by ID",
            description = "Fetches a single lab interpretation by its ID for the given user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lab interpretation found",
                    content = @Content(schema = @Schema(implementation = LabInterpretationResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Lab interpretation not found or access denied",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<LabInterpretationResponseDto> getLabInterpretationById(
            @Parameter(description = "Lab interpretation ID", required = true)
            @PathVariable String id,

            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LabInterpretationResponseDto dto = labInterpretationService.getByIdAndUser(id, userDetails.getId());
        return ResponseEntity.ok(dto);
    }

}
