package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
public class LabInterpretationResponseDto {

    private String userId;
    private Instant createdAt;
    private String plainLanguageSummary;
    private List<String> recommendations;
    private List<LabInterpretationResultDataDto> resultsData;
}