package com.psr.nosql.service;

import com.psr.nosql.util.Base62Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShortCodeGenerator {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String SEQ_KEY = "short-url:sequence";

    public String generate() {
        Long seq = stringRedisTemplate.opsForValue().increment(SEQ_KEY);

        if (seq == null) {
            throw new IllegalStateException("Redis sequence is null");
        }

        return Base62Util.encode(seq);
    }
}
