package com.psr.nosql.service;

import com.psr.nosql.cache.RedisShortUrlCache;
import com.psr.nosql.dto.ShortUrlResponse;
import com.psr.nosql.dto.UrlRequestDto;
import com.psr.nosql.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final ShortUrlRepository shortUrlRepo;
    private final RedisShortUrlCache redisCache;
    private final ShortCodeGenerator codeGenerator;

    public ShortUrlResponse createShortUrl(UrlRequestDto request) {

    }
}
