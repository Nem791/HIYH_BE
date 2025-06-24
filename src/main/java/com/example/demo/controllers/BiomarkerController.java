package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.models.BiomarkerRecord;
import com.example.demo.services.BiomarkerService;
import com.example.demo.services.LabInterpretationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/biomarkers")
public class BiomarkerController {
    private final BiomarkerService biomarkerService;
    private final LabInterpretationService labInterpretationService;

    @Autowired
    public BiomarkerController(BiomarkerService biomarkerService, LabInterpretationService labInterpretationService) {
        this.biomarkerService = biomarkerService;
        this.labInterpretationService = labInterpretationService;
    }

    @PostMapping()
    public ResponseEntity<BiomarkerRecord> createBiomarkerRecord(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute BiomarkerFormDto biomarkerData) {
        BiomarkerRecord record = biomarkerService.createBiomarkerRecord(file, biomarkerData);
        return ResponseEntity.status(201).body(record); // HTTP 201 Created, body = created document
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<GptResponse> getLatestBiomarkerRecordsAndInterpret(
//            @PathVariable String userId,
//            @RequestParam(name = "limit", defaultValue = "5") int limit) {
//
//        // Step 1: Get biomarker records as DTOs
//        List<BiomarkerRecord> records = biomarkerService.getLatestBiomarkerRecords(userId, limit);
//        List<BiomarkerRecordDto> dtos = biomarkerService.toDtoList(records);
//
//        // Step 2: Hardcode the patient info
//        PatientInfoDto patientInfo = new PatientInfoDto();
//        patientInfo.setAge(28);
//        patientInfo.setGender("Male");
//        patientInfo.setRace("Chinese");
//        patientInfo.setChronicDisease(List.of("Heart disease"));
//        patientInfo.setWeight(170);
//        patientInfo.setHeight(180);
//
//        // Step 3: Assemble request for interpretation service
//        LabInterpretationDataRequest dataRequest = new LabInterpretationDataRequest();
//        dataRequest.setLabResultData(dtos);
//        dataRequest.setPatientInfo(patientInfo);
//
//        GptRequest request = new GptRequest();
//        request.setData(dataRequest);
//        // Set other GptRequest fields (if needed)
//
//        // Step 4: Get response from interpretation service
//        GptResponse gptResponse = labInterpretationService.createLabInterpretation(request);
//
//        return ResponseEntity.ok(gptResponse);
//    }
}