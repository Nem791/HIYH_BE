package com.example.demo.repository;
import com.example.demo.dto.response.LabInterpretationRecentListDto;

import org.springframework.data.domain.Page;
import com.example.demo.dto.request.SortAndFilterDto;

public interface LabInterpretationRepositoryCustom {
    Page<LabInterpretationRecentListDto> findRecentByUserId(
        String userId, int page, int size, SortAndFilterDto sortAndFilterDto
    );
}
