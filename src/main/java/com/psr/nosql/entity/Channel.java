package com.psr.nosql.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Builder
@Document(collection = "channels")
public class Channel {
    @Id
    private String id;
    private String channelName;
    private int visitorCount;

    @CreatedDate
    private Instant createdAt;
}
