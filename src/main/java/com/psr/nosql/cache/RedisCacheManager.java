package com.psr.nosql.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisCacheManager {

    private final RedisTemplate redisTemplate;

    public Optional<String> getCachedUrl(String prefix, String id) {
        Object value = redisTemplate.opsForValue().get(prefix + id);
        return (value instanceof String) ? Optional.of((String) value) : Optional.empty();
    }

    // ✅ Redis에 URL 캐싱 (TTL 적용)
    public void cacheUrl(String prefix, String id, String url, Duration ttl) {
        redisTemplate.opsForValue().set(prefix + id, url, ttl);
    }

    // ✅ 조회 횟수 증가 후 반환
    public Long incrementVisitCount(String prefix, String id) {
        return redisTemplate.opsForValue().increment(prefix + id);
    }
}
