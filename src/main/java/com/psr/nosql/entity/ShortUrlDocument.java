package com.psr.nosql.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "short_urls")
public class ShortUrlDocument {

    @Id
    private String shortCode;

    private String originalUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
