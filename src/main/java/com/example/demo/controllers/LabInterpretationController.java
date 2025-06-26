package com.example.demo.controllers;

import com.example.demo.dto.request.BiomarkerFormDto;
import com.example.demo.dto.response.ApiErrorResponse;
import com.example.demo.dto.response.LabInterpretationResponseDto;
import com.example.demo.models.BiomarkerRecord;
import com.example.demo.models.LabInterpretation;
import com.example.demo.services.LabInterpretationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    public Page<LabInterpretation> getLabInterpretations(
            @Parameter(description = "User ID to filter lab interpretations", example = "abc123", required = true)
            @RequestParam String userId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (number of records per page)", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return labInterpretationService.getLabInterpretations(userId, page, size);
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
            @RequestPart(value = "userId") String userId
    ) {
        BiomarkerFormDto dto = new BiomarkerFormDto();
        dto.setUserId(userId);

        LabInterpretationResponseDto response =
                labInterpretationService.createLabInterpretation(file, dto);

        return ResponseEntity.ok(response);
    }

}
