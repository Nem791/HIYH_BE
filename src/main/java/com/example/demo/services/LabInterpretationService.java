package com.example.demo.services;

import com.example.demo.dto.request.BiomarkerFormDto;
import com.example.demo.constants.AppConstants;
import com.example.demo.dto.GptRequest;
import com.example.demo.dto.request.PatientInfoDto;
import com.example.demo.dto.request.SortAndFilterDto;
import com.example.demo.dto.response.LabInterpretationResponseDto;
import com.example.demo.dto.response.LabInterpretationRecentListDto;
import com.example.demo.exceptions.GptResponseParseException;
import com.example.demo.exceptions.ResourceNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class LabInterpretationService {
    private final AzureOpenAiService azureOpenAiService;
    private final BiomarkerService biomarkerService;
    private final UserService userService;
    private final GptRequestBuilderService gptRequestBuilderService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final LabInterpretationRepository labInterpretationRepository;


    public LabInterpretationService(AzureOpenAiService azureOpenAiService, BiomarkerService biomarkerService, UserService userService,
                                    GptRequestBuilderService gptRequestBuilderService, ObjectMapper objectMapper, ModelMapper modelMapper,
                                    LabInterpretationRepository labInterpretationRepository) {
        this.azureOpenAiService = azureOpenAiService;
        this.biomarkerService = biomarkerService;
        this.userService = userService;
        this.gptRequestBuilderService = gptRequestBuilderService;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.labInterpretationRepository = labInterpretationRepository;
    }

    public LabInterpretationResponseDto getByIdAndUser(String id, String userId) {
        LabInterpretation labInterpretation = labInterpretationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "LabInterpretation with ID " + id + " not found or access denied"));

        return modelMapper.map(labInterpretation, LabInterpretationResponseDto.class);
    }

    public Page<LabInterpretationRecentListDto> getLabInterpretations(
            String userId, SortAndFilterDto sortAndFilterDto
    ) {
        if (sortAndFilterDto == null)
            sortAndFilterDto = new SortAndFilterDto();
        return labInterpretationRepository.findRecentByUserId(
                userId,
                sortAndFilterDto
        );
    }

    public LabInterpretationResponseDto createLabInterpretation(MultipartFile file, String userId) {
        BiomarkerFormDto dto = new BiomarkerFormDto();
        dto.setUserId(userId);

        // 1. Save the record
        BiomarkerRecord record = biomarkerService.createBiomarkerRecord(file, dto);

        // 2. Get recent records
        List<BiomarkerRecord> recentBiomarkerRecords = biomarkerService
                .getLatestBiomarkerRecords(userId, 0, 1).getContent();
        // 3. Get or mock patient info
        PatientInfoDto patientInfo = modelMapper.map(userService.getUserProfile(userId), PatientInfoDto.class);

        // 4. Build GPT request
        GptRequest request = gptRequestBuilderService.buildAnalyzeLabResultPrompt(recentBiomarkerRecords, patientInfo);

        String rawGptResponse = azureOpenAiService.fetchGptEndpoint(request);

        try {
            // Step 1 - Parse the raw GPT response (JSON) into a LabInterpretation object
            LabInterpretation labInterpretation = objectMapper.readValue(rawGptResponse, LabInterpretation.class);

            // Step 2 - Set LabInterpretation metadata and link it to the user and biomarker record
            labInterpretation.setCreatedAt(Instant.now());
            labInterpretation.setReportedOn(record.getReportedOn());
            labInterpretation.setUserId(userId);
            labInterpretation.setBiomarkerRecordId(record.getId());
            // set testType and testName to these defaults for now
            labInterpretation.setTestType(AppConstants.DEFAULT_TEST_TYPE);
            labInterpretation.setTestName(AppConstants.DEFAULT_TEST_NAME);

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
