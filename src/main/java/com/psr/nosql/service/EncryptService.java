package com.psr.nosql.service;

import com.psr.nosql.constant.MessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EncryptService {

    private final RedisTemplate redisTemplate;

    public String saveAndReturnEncKey(String origin, long timeout) {

        String key = getEncKey(origin);

        redisTemplate.opsForValue().set(key, origin, timeout, TimeUnit.SECONDS);

        return key;
    }

    private String getEncKey(String origin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(origin.getBytes(StandardCharsets.UTF_8));

            String encoded = Base64.getUrlEncoder().encodeToString(digest);

            return encoded.substring(0, 5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(MessageConstant.ALGORITHM_NOT_FOUND);
        }
    }

    public String getOriginByEncStr(String encStr) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(encStr))
                .map(Objects::toString)
                .orElseThrow(() -> new RuntimeException(MessageConstant.NOT_FOUND));
    }
}
