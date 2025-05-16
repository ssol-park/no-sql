package com.psr.nosql.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoIndexInitializer {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void createTtlIndex() {
        // short_urls 컬렉션에 expiresAt 필드 기준 TTL 인덱스 생성
        Index ttlIndex = new Index()
                .on("expiresAt", Sort.Direction.ASC)
                .expire(0); // expiresAt 기준으로 즉시 만료

        mongoTemplate.indexOps("short_urls")
                .ensureIndex(ttlIndex);
    }
}
