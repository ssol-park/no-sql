package com.psr.nosql.service;

import com.psr.nosql.util.Base62Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ShortCodeGenerator {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String SEQ_KEY = "short-url:sequence";
    private static final int MAX_RETRY = 5;


    public String generate() {
        Long seq = stringRedisTemplate.opsForValue().increment(SEQ_KEY);

        if (seq == null) {
            throw new IllegalStateException("Redis sequence is null");
        }

        return Base62Util.encode(seq);
    }

    public String generate(String originalUrl, String salt) {
        String input = originalUrl + salt;

        byte[] hash = sha256(input);
        byte[] shortBytes = Arrays.copyOfRange(hash, 0, 6);

        return Base62Util.encode(shortBytes);
    }

    private byte[] sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available");
        }
    }
}
