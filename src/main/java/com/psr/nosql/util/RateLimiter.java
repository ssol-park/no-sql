package com.psr.nosql.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimiter {

    private static final String PREFIX = "rate_limit:";
    private static final long WINDOW_SIZE = 60; // 60초 (1분)
    private static final int LIMIT = 10; // 2분당 최대 10회 요청
    private static final long TTL = 80; // TTL

    private final RedisTemplate<String, String> redisTemplate;
    private final DefaultRedisScript<Long> rateLimiterScript;

    public void reset(String userId) {
    }

    public boolean isAllowed(String userId) {
        String key = PREFIX + userId;
        long now = System.currentTimeMillis();

        // execute(RedisScript<T> script, List<K> keys, Object... args)
        Long result = redisTemplate.execute(rateLimiterScript, List.of(key),
                String.valueOf(now), String.valueOf(WINDOW_SIZE),
                String.valueOf(LIMIT), String.valueOf(TTL));

        boolean allowed = result != null && result == 1;

        log.info("사용자 {}의 요청 결과: {} (현재 시간: {}, 키: {})", userId, allowed ? "허용됨" : "차단됨", now, key);

        return allowed;
    }
}
