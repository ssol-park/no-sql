package com.psr.nosql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psr.nosql.entity.Page;
import com.psr.nosql.repository.PageRepository;
import com.psr.nosql.service.PageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PageTest {

    @Autowired
    private PageService pageService;
    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String URL_CACHE_KEY = "cached_url:";
    private static final String VISIT_COUNT_KEY = "visit_count:";

    @BeforeAll
    void beforeAll() throws IOException {
        boolean existsData = pageRepository.exists(Example.of(new Page()));

        if (!existsData) {
            var resource = new ClassPathResource("/sample/url.json");
            List<Page> pages = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});

            pageRepository.saveAll(pages);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"google", "chatgpt", "github", "stackoverflow", "youtube"})
    void ID_조회_및_캐싱_테스트(String id) {

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        IntStream.rangeClosed(1, 5)
                .forEach(num -> {
                    String url = pageService.getUrl(id);
                    assertNotNull(url);

                    String countStr = ops.get(VISIT_COUNT_KEY + id);
                    assertNotNull(countStr);

                    int visitCount = Integer.parseInt(countStr);
                    assertEquals(num, visitCount);
                });

        String cachedUrl = ops.get(URL_CACHE_KEY + id);
        assertNotNull(cachedUrl);
    }
}
