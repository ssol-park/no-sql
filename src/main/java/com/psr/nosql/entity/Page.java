package com.psr.nosql.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Getter
@Builder
@Document(collection = "pages")
public class Page {
    @MongoId
    private String id;
    private String url;

    @CreatedDate
    private Instant createdAt;
}
