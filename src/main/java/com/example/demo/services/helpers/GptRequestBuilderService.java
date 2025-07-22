package com.example.demo.services.helpers;

import com.example.demo.constants.AppConstants;
import com.example.demo.dto.GptMessage;
import com.example.demo.dto.GptRequest;
import com.example.demo.dto.request.LabResultWithPatientInfoDto;
import com.example.demo.dto.request.PatientInfoDto;
import com.example.demo.models.BiomarkerRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class GptRequestBuilderService {

    private final ObjectMapper objectMapper;
    private Map<String, Object> biomarkerResponseFormat;
    private Map<String, Object> labInterpretationResponseFormat;

    @Autowired
    public GptRequestBuilderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void init() {
        this.biomarkerResponseFormat = loadJson("/utils/biomarker_response.json");
        this.labInterpretationResponseFormat = loadJson("/utils/lab_interpretation_response.json");
    }

    public GptRequest buildBiomarkerExtractionPrompt(String userMessage) {
        return getGptRequest(AppConstants.BIOMARKER_EXTRACTION_SYSTEM_PROMPT, userMessage, biomarkerResponseFormat);
    }

    public GptRequest buildAnalyzeLabResultPrompt(List<BiomarkerRecord> recentBiomarkerRecords, PatientInfoDto patientInfo) {
        // Mock/fill patient data
//        generateMockPatientData(patientInfo);

        LabResultWithPatientInfoDto requestDto = new LabResultWithPatientInfoDto();
        requestDto.setLabResultData(recentBiomarkerRecords);
        requestDto.setPatientInfo(patientInfo);

        String userMessage;
        try {
            userMessage = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize LabResultWithPatientInfoDto", e);
        }

        return getGptRequest(AppConstants.LAB_INTERPRETATION_SYSTEM_PROMPT, userMessage, labInterpretationResponseFormat);
    }

    private Map<String, Object> loadJson(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("File not found: " + path);
            }
            return objectMapper.readValue(is, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error loading JSON from: " + path, e);
        }
    }

    private void generateMockPatientData(PatientInfoDto patientInfo) {
        patientInfo.setSexAtBirth("Male");
        patientInfo.setRace("Chinese");
        patientInfo.setChronicDisease(List.of("Heart disease"));
        patientInfo.setWeight(170.0);
        patientInfo.setHeight(180.0);
    }

    private GptRequest getGptRequest(String systemMessage, String userMessage, Map<String, Object> responseFormat) {
        List<GptMessage> messages = List.of(
                new GptMessage(AppConstants.ROLE_SYSTEM, systemMessage),
                new GptMessage(AppConstants.ROLE_USER, userMessage)
        );

        GptRequest req = new GptRequest();
        req.setMessages(messages);
        req.setResponse_format(responseFormat);
        return req;
    }
}