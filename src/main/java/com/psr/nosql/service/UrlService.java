package com.psr.nosql.service;

import com.psr.nosql.cache.RedisShortUrlCache;
import com.psr.nosql.dto.ShortUrlResponse;
import com.psr.nosql.dto.UrlRequestDto;
import com.psr.nosql.entity.ShortUrlDocument;
import com.psr.nosql.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final ShortUrlRepository shortUrlRepo;
    private final RedisShortUrlCache redisCache;
    private final ShortCodeGenerator codeGenerator;

    private static final int TTL = 3600;

    public ShortUrlResponse createShortUrl(UrlRequestDto request) {

        String originalUrl = request.getOriginalUrl();
        String shortCode = codeGenerator.generate();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expire = now.plusSeconds(TTL);

        ShortUrlDocument shortUrlDocument = ShortUrlDocument.builder()
                .originalUrl(originalUrl)
                .shortCode(shortCode)
                .createdAt(now)
                .expiresAt(expire)
                .build();

        shortUrlRepo.save(shortUrlDocument);

        redisCache.save(shortCode, originalUrl, Duration.ofSeconds(TTL));

        return ShortUrlResponse
    }
}
