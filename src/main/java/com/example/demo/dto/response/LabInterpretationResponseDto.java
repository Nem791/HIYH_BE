package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class LabInterpretationResponseDto {

    private String id;
    private String userId;
    private Instant createdAt;
    private Date reportedOn;
    private String testType;
    private String testName;
    private String plainLanguageSummary;
    private String plainLanguageSummaryShort;
    private List<String> recommendations;
    private List<LabInterpretationResultDataDto> resultsData;
}