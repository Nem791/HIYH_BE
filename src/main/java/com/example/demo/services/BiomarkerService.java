package com.example.demo.services;

import com.example.demo.dto.BiomarkerFormDto;
import com.example.demo.dto.GptRequest;
import com.example.demo.dto.GptResponse;
import com.example.demo.exceptions.GptResponseParseException;
import com.example.demo.exceptions.PdfProcessingException;
import com.example.demo.models.BiomarkerRecord;
import com.example.demo.models.BiomarkerValue;
import com.example.demo.repository.BiomarkerRecordRepository;
import com.example.demo.utils.GptRequestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

@Service
public class BiomarkerService {

    private final BiomarkerRecordRepository biomarkerRecordRepository;
    private final AzureOpenAiService azureOpenAiService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BiomarkerService(BiomarkerRecordRepository biomarkerRecordRepository, AzureOpenAiService azureOpenAiService, ObjectMapper objectMapper) {
        this.biomarkerRecordRepository = biomarkerRecordRepository;
        this.azureOpenAiService = azureOpenAiService;
        this.objectMapper = objectMapper;
    }

    /**
     * Create and save a new BiomarkerRecord.
     * @param file the record to save
     * @return the saved record (with generated id)
     */
    public BiomarkerRecord createBiomarkerRecord(MultipartFile file, BiomarkerFormDto biomarkerData) {
        String rawLabResultText = extractTextFromPdf(file); // pdf errors converted to PdfProcessingException
        try {
            GptRequest biomarkerExtractionPrompt = GptRequestUtils.biomarkerExtractionRequest(rawLabResultText);
            String rawTextResponse = azureOpenAiService.fetchGptEndpoint(biomarkerExtractionPrompt);
            Map<String, BiomarkerValue> biomarkers = objectMapper.readValue(
                    rawTextResponse,
                    new TypeReference<>() {}
            );
            BiomarkerRecord record = new BiomarkerRecord();
            record.setUserId(biomarkerData.getUserId());
            record.setBiomarkers(biomarkers);
            return biomarkerRecordRepository.save(record);
        } catch (JsonProcessingException e) {
            throw new GptResponseParseException("Failed to parse OpenAI response", e);
        }
    }

    public GptResponse createLabInterpretation(GptRequest request) {

        String rawGptResponse = azureOpenAiService.fetchGptEndpoint(request);
        try {
            // Happy-path: deserialize into your Java class
            return objectMapper.readValue(rawGptResponse, GptResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse response as GptResponse", e);
        }
    }

    public String extractTextFromPdf(MultipartFile file) {
        if (file.isEmpty() || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(".pdf")) {
            throw new PdfProcessingException("Please upload a PDF file.");
        }
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            return text.replace("\r\n", "\n").replace("\r", "\n").replace("\n", "\\n");
        } catch (IOException e) {
            throw new PdfProcessingException("Could not process PDF", e);
        }
    }
}