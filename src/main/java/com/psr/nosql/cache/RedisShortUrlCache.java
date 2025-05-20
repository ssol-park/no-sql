package com.psr.nosql.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisShortUrlCache {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "url:";

    public void save(String shortCode, String originalUrl, Duration ttl) {
        redisTemplate.opsForValue().set(PREFIX + shortCode, originalUrl, ttl);
    }

    public String get(String shortCode) {
        return redisTemplate.opsForValue().get(PREFIX + shortCode);
    }

    public void delete(String shortCode) {
        redisTemplate.delete(PREFIX + shortCode);
    }
}
