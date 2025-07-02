package com.example.demo.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Document(collection = "lab_interpretations")
public class LabInterpretation {

    @Id
    private String id;

    @NotNull
    private String userId;

    @CreatedDate
    private Instant createdAt;

    @NotEmpty
    private String plainLanguageSummary;

    @NotEmpty
    private List<String> recommendations;

    @NotEmpty
    private List<LabInterpretationResultData> resultsData;

}
