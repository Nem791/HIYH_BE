package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class LabInterpretationRecentListDto {
    private String id;
    private String userId;
    private Date createdAt;
    private Date reportedOn;
    private int abnormalBiomarkers;
    private String testType;
    private String testName;
}
