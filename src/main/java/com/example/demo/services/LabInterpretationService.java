package com.example.demo.services;

import com.example.demo.dto.request.BiomarkerFormDto;
import com.example.demo.dto.GptRequest;
import com.example.demo.dto.request.PatientInfoDto;
import com.example.demo.dto.response.LabInterpretationResponseDto;
import com.example.demo.exceptions.GptResponseParseException;
import com.example.demo.models.BiomarkerRecord;
import com.example.demo.models.BiomarkerValue;
import com.example.demo.models.LabInterpretation;
import com.example.demo.repository.LabInterpretationRepository;
import com.example.demo.services.helpers.GptRequestBuilderService;
import com.example.demo.utils.BiomarkerUtils;
import com.example.demo.utils.LabInterpretationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class LabInterpretationService {
    private final AzureOpenAiService azureOpenAiService;
    private final BiomarkerService biomarkerService;
    private final GptRequestBuilderService gptRequestBuilderService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final LabInterpretationRepository labInterpretationRepository;

    public LabInterpretationService(AzureOpenAiService azureOpenAiService, BiomarkerService biomarkerService, GptRequestBuilderService gptRequestBuilderService, ObjectMapper objectMapper, ModelMapper modelMapper, LabInterpretationRepository labInterpretationRepository) {
        this.azureOpenAiService = azureOpenAiService;
        this.biomarkerService = biomarkerService;
        this.gptRequestBuilderService = gptRequestBuilderService;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.labInterpretationRepository = labInterpretationRepository;
    }

    public Page<LabInterpretation> getLabInterpretations(String userId, int page, int size) {
        return labInterpretationRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size));
    }

    public LabInterpretationResponseDto createLabInterpretation(MultipartFile file, BiomarkerFormDto biomarkerData) {
        // 1. Save the record
        BiomarkerRecord record = biomarkerService.createBiomarkerRecord(file, biomarkerData);

        // 2. Get recent records
        List<BiomarkerRecord> recentBiomarkerRecords =
                biomarkerService.getLatestBiomarkerRecords(biomarkerData.getUserId(), 0, 5).getContent();
        // 3. Get or mock patient info
        PatientInfoDto patientInfo = new PatientInfoDto();

        // 4. Build GPT request
        GptRequest request = gptRequestBuilderService.buildAnalyzeLabResultPrompt(recentBiomarkerRecords, patientInfo);

        String rawGptResponse = azureOpenAiService.fetchGptEndpoint(request);

        try {
            // Step 1
            LabInterpretation labInterpretation = objectMapper.readValue(rawGptResponse, LabInterpretation.class);

            // Step 2
            labInterpretation.setCreatedAt(Instant.now());
            labInterpretation.setUserId(biomarkerData.getUserId());

            // Step 3 - Enrich from merged biomarker values
            Map<String, BiomarkerValue> merged = BiomarkerUtils.mergeBiomarkersWithPriority(recentBiomarkerRecords);
            LabInterpretationUtils.enrichLabInterpretationWithBiomarkerValues(labInterpretation, merged);

            // Step 4: Save to MongoDB
            LabInterpretation saved = labInterpretationRepository.save(labInterpretation);

            // Step 5: Convert saved entity to response DTO
            return modelMapper.map(saved, LabInterpretationResponseDto.class);

        } catch (JsonProcessingException e) {
            throw new GptResponseParseException("Failed to parse GPT-4.1 response into LabInterpretation", e);
        }
    }
}
