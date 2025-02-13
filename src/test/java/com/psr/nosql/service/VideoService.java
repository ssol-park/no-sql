package com.psr.nosql.service;

import com.psr.nosql.dto.VideoUrlDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class VideoService {

    private final RedisTemplate redisTemplate;
    private final ValueOperations<String, String> valueOps;

    private static final String VIDEO_KEY_PREFIX = "video:";
    private static final String VIDEO_VIEW_COUNT_PREFIX = "video:viewCount:";

    public VideoService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    public long incrementViewCount(String key) {
        return valueOps.increment(key);
    }

    public String getUrl(String key) {
        return (String) redisTemplate.opsForHash().get(key, "url");
    }

    public VideoUrlDto incrementViewAndReturnUrl(String videoId) {
        incrementViewCount(VIDEO_VIEW_COUNT_PREFIX + videoId);

        return new VideoUrlDto(getUrl(VIDEO_KEY_PREFIX + videoId));
    }
}
