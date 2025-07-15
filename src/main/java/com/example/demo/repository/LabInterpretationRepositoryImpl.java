package com.example.demo.repository;

import com.example.demo.dto.response.LabInterpretationRecentListDto;
import com.example.demo.models.LabInterpretation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.example.demo.models.SortBy;
import com.example.demo.models.TestType;


public class LabInterpretationRepositoryImpl implements LabInterpretationRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<LabInterpretationRecentListDto> findRecentByUserId(
            String userId, int page, int size,
            SortBy sortBy, Sort.Direction sortOrder, String startDate, String endDate, boolean onlyAbnormal, TestType testType
    ) {
        // filter by userId and testType
        Criteria matchCriteria = Criteria.where("userId").is(userId).and("testType").is(testType.getValue());

        // start date filtering
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ssX";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

        // convert string dates to Date objects if provided
        Date startDateFormatted = isValidDate(startDate, formatter) ? Date.from(ZonedDateTime.parse(startDate, formatter).toInstant()) : null;
        Date endDateFormatted = isValidDate(endDate, formatter) ? Date.from(ZonedDateTime.parse(endDate, formatter).toInstant()) : null;
        
        if(startDateFormatted != null && endDateFormatted != null) {
                matchCriteria = matchCriteria.and("reportedOn").gte(startDateFormatted).lte(endDateFormatted);
        } else if (startDateFormatted != null) {
                matchCriteria = matchCriteria.and("reportedOn").gte(startDateFormatted);
        } else if (endDateFormatted != null) {
                matchCriteria = matchCriteria.and("reportedOn").lte(endDateFormatted);
        }

        // filter only abnormal biomarkers if requested
        if (onlyAbnormal) {
            matchCriteria = matchCriteria.and("abnormalBiomarkers").gt(0);
        }

        MatchOperation match = Aggregation.match(matchCriteria);
        
        String sortByValue = sortBy.getValue();
        SortOperation sort = Aggregation.sort(Sort.by(sortOrder, sortByValue));

        ProjectionOperation project = Aggregation.project("id", "userId", "createdAt", "reportedOn", "testType", "testName").and(
                ArrayOperators.Size.lengthOfArray(
                        ArrayOperators.Filter.filter("resultsData")
                                .as("entry")
                                .by(ComparisonOperators.Ne.valueOf("$$entry.classification").notEqualToValue("green"))
                )
        ).as("abnormalBiomarkers");

        SkipOperation skip = Aggregation.skip((long) page * size);
        LimitOperation limit = Aggregation.limit(size);

        Aggregation aggregation = Aggregation.newAggregation(project, match, sort, skip, limit);

        List<LabInterpretationRecentListDto> results = mongoTemplate.aggregate(
                aggregation,
                "lab_interpretations", // Mongo collection name (can use @Document value too)
                LabInterpretationRecentListDto.class
        ).getMappedResults();

        long total = mongoTemplate.count(
                Query.query(matchCriteria),
                LabInterpretation.class
        );

        return new PageImpl<>(results, PageRequest.of(page, size), total);
    }

    private boolean isValidDate(String dateStr, DateTimeFormatter formatter) {
        if (dateStr == null) return false;
        try {
            ZonedDateTime.parse(dateStr, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
