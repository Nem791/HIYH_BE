package com.example.demo.filter;

import com.example.demo.repository.LabInterpretationRepository;
import org.springframework.stereotype.Component;

@Component("ownershipSecurity")
public class OwnershipSecurity {
    private final LabInterpretationRepository labRepo;

    public OwnershipSecurity(LabInterpretationRepository labRepo) {
        this.labRepo = labRepo;
    }

    public boolean isOwner(String recordId, String userId) {
        return labRepo.findById(recordId)
                .map(lab -> lab.getUserId().equals(userId))
                .orElse(false);
    }
}