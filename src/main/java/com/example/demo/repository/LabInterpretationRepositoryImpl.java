package com.example.demo.repository;

import com.example.demo.dto.response.LabInterpretationRecentListDto;
import com.example.demo.models.LabInterpretation;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

public class LabInterpretationRepositoryImpl implements LabInterpretationRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<LabInterpretationRecentListDto> findRecentByUserId(String userId, int page, int size) {
        MatchOperation match = Aggregation.match(Criteria.where("userId").is(userId));
        SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.DESC, "createdAt"));

        ProjectionOperation project = Aggregation.project("id", "userId", "createdAt", "reportedOn")
                .and(
                        ArrayOperators.Size.lengthOfArray(
                                ArrayOperators.Filter.filter("resultsData")
                                        .as("entry")
                                        .by(ComparisonOperators.Ne.valueOf("$$entry.classification").notEqualToValue("green"))
                        )
                ).as("abnormalBiomarkers");

        SkipOperation skip = Aggregation.skip((long) page * size);
        LimitOperation limit = Aggregation.limit(size);

        Aggregation aggregation = Aggregation.newAggregation(match, sort, project, skip, limit);

        List<LabInterpretationRecentListDto> results = mongoTemplate.aggregate(
                aggregation,
                "lab_interpretations", // Mongo collection name (can use @Document value too)
                LabInterpretationRecentListDto.class
        ).getMappedResults();

        long total = mongoTemplate.count(
                Query.query(Criteria.where("userId").is(userId)),
                LabInterpretation.class
        );

        return new PageImpl<>(results, PageRequest.of(page, size), total);
    }
}
