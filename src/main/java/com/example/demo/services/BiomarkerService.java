package com.example.demo.services;

import com.example.demo.dto.request.BiomarkerFormDto;
import com.example.demo.dto.GptRequest;
import com.example.demo.exceptions.GptResponseParseException;
import com.example.demo.models.BiomarkerRecord;
import com.example.demo.models.BiomarkerValue;
import com.example.demo.repository.BiomarkerRecordRepository;
import com.example.demo.services.helpers.GptRequestBuilderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class BiomarkerService {

    private final BiomarkerRecordRepository biomarkerRecordRepository;
    private final AzureOpenAiService azureOpenAiService;
    private final GptRequestBuilderService gptRequestBuilderService;
    private final ObjectMapper objectMapper;
    private final PdfService pdfService;

    @Autowired
    public BiomarkerService(BiomarkerRecordRepository biomarkerRecordRepository, AzureOpenAiService azureOpenAiService, GptRequestBuilderService gptRequestBuilderService, ObjectMapper objectMapper, PdfService pdfService) {
        this.biomarkerRecordRepository = biomarkerRecordRepository;
        this.azureOpenAiService = azureOpenAiService;
        this.gptRequestBuilderService = gptRequestBuilderService;
        this.objectMapper = objectMapper;
        this.pdfService = pdfService;
    }

    /**
     * Create and save a new BiomarkerRecord.
     *
     * @param file the record to save
     * @return the saved record (with generated id)
     */
    //    Use lombok later
    public BiomarkerRecord createBiomarkerRecord(MultipartFile file, BiomarkerFormDto biomarkerData) {
        String rawLabResultText = pdfService.extractTextFromPdf(file);
        Map<String, BiomarkerValue> biomarkers = extractBiomarkersFromText(rawLabResultText);

        BiomarkerRecord record = new BiomarkerRecord();
        record.setUserId(biomarkerData.getUserId());
        record.setBiomarkers(biomarkers);
        record.setReportedOn(biomarkers.values().iterator().next().getTimeResulted());
        return biomarkerRecordRepository.save(record);
    }

    public Map<String, BiomarkerValue> extractBiomarkersFromText(String labResultText) {
        GptRequest biomarkerExtractionPrompt = gptRequestBuilderService.buildBiomarkerExtractionPrompt(labResultText);
        String rawResponse = azureOpenAiService.fetchGptEndpoint(biomarkerExtractionPrompt);
        try {
            return objectMapper.readValue(rawResponse, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new GptResponseParseException("Failed to parse OpenAI response", e);
        }
    }

    public Page<BiomarkerRecord> getLatestBiomarkerRecords(String userId, int page, int size) {
        return biomarkerRecordRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size));
    }
}