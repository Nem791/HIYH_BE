package com.example.demo.repository;

import com.example.demo.dto.response.LabInterpretationRecentListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import com.example.demo.models.SortBy;
import com.example.demo.models.TestType;

public interface LabInterpretationRepositoryCustom {
    Page<LabInterpretationRecentListDto> findRecentByUserId(
        String userId, int page, int size,
        SortBy sortBy, Sort.Direction sortOrder, String startDate, String endDate, boolean onlyAbnormal, TestType testType
    );
}
