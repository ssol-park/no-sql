package com.psr.nosql.service;

import com.psr.nosql.cache.RedisCacheManager;
import com.psr.nosql.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final RedisCacheManager redisCacheManager;

    private static final String URL_CACHE_KEY = "cached_url:";
    private static final String VISIT_COUNT_KEY = "visit_count:";
    private static final int CACHE_THRESHOLD = 5;
    private static final Duration CACHE_TTL = Duration.ofHours(24);


    public String getUrl(String id) {
        return redisCacheManager.getCachedUrl(URL_CACHE_KEY, id)
                .orElseGet(() -> getUrlFromDB(id));
    }

    private String getUrlFromDB(String id) {
        Long visitCount = redisCacheManager.incrementVisitCount(VISIT_COUNT_KEY, id);

        return pageRepository.findById(id)
                .map(page -> {
                    if (visitCount != null && visitCount >= CACHE_THRESHOLD) {
                        redisCacheManager.cacheUrl(URL_CACHE_KEY, id, page.getUrl(), CACHE_TTL);
                    }

                    return page.getUrl();
                })
                .orElseGet(String::new);
    }
}
