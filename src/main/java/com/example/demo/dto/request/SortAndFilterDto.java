package com.example.demo.dto.request;

import com.example.demo.constants.SortBy;
import com.example.demo.constants.TestType;
import org.springframework.data.domain.Sort;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SortAndFilterDto {
    private int page = 0;
    private int size = 20;
    private SortBy sortBy = SortBy.REPORTED_ON;
    private Sort.Direction sortOrder = Sort.Direction.DESC;
    private String startDate;
    private String endDate;
    private boolean onlyAbnormal = false;
    private List<TestType> testTypes = new ArrayList<>(List.of(TestType.BLOOD_TEST));
}
