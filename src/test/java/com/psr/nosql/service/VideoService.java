package com.psr.nosql.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class VideoService {

    private final RedisTemplate redisTemplate;
    private final ValueOperations<String, String> valueOps;

    public VideoService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    public long incrementViewCount(String key) {
        return valueOps.increment(key);
    }
}
