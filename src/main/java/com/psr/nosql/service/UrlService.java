package com.psr.nosql.service;

import com.psr.nosql.cache.RedisShortUrlCache;
import com.psr.nosql.config.BaseProperties;
import com.psr.nosql.dto.ShortUrlResponse;
import com.psr.nosql.dto.UrlRequestDto;
import com.psr.nosql.entity.ShortUrlDocument;
import com.psr.nosql.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {

    private final ShortUrlRepository shortUrlRepo;
    private final RedisShortUrlCache redisCache;
    private final ShortCodeGenerator codeGenerator;
    private final BaseProperties baseProperties;

    private static final int TTL = 3600;
    private static final int MAX_RETRY = 5;

    public ShortUrlResponse createShortUrl(UrlRequestDto request) {

        String originalUrl = request.getOriginalUrl();

        for (int i = 0; i < MAX_RETRY; i++) {
            String salt = (i == 0) ? "" : UUID.randomUUID().toString();
            String shortCode = codeGenerator.generate(originalUrl, salt);

            // Redis 확인
            String cached = redisCache.get(shortCode);
            if (cached != null) {
                if (cached.equals(originalUrl)) {
                    return ShortUrlResponse.of(baseProperties.getUrl(), shortCode); // 동일 URL → 재사용
                }
                continue; // 충돌 → 다음 salt
            }

            // MongoDB 확인
            ShortUrlDocument existing = shortUrlRepo.findById(shortCode)
                    .filter(doc -> doc.getOriginalUrl().equals(originalUrl))
                    .orElse(null);

            if (existing != null) {
                return ShortUrlResponse.of(baseProperties.getUrl(), shortCode); // 동일 URL → 재사용
            }

            if (shortUrlRepo.existsById(shortCode)) {
                continue; // 충돌 → 다음 salt
            }

            // 저장
            LocalDateTime now = LocalDateTime.now();
            ShortUrlDocument doc = ShortUrlDocument.builder()
                    .shortCode(shortCode)
                    .originalUrl(originalUrl)
                    .createdAt(now)
                    .expiresAt(now.plusSeconds(TTL))
                    .build();

            shortUrlRepo.save(doc);
            redisCache.save(shortCode, originalUrl, Duration.ofSeconds(TTL));

            return ShortUrlResponse.of(baseProperties.getUrl(), shortCode);
        }

        throw new IllegalStateException("단축 URL 생성 실패 (충돌 반복)");
    }
}
