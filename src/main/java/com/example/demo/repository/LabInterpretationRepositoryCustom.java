package com.example.demo.repository;

import com.example.demo.constants.SortBy;
import com.example.demo.constants.TestType;
import com.example.demo.dto.response.LabInterpretationRecentListDto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface LabInterpretationRepositoryCustom {
    Page<LabInterpretationRecentListDto> findRecentByUserId(
        String userId, int page, int size,
        SortBy sortBy, Sort.Direction sortOrder, String startDate, String endDate, boolean onlyAbnormal, List<TestType> testTypes
    );
}
