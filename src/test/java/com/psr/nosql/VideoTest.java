package com.psr.nosql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psr.nosql.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VideoTest {

    private static final String VIDEO_KEY_PREFIX = "video:";
    private static final String VIDEO_VIEW_COUNT_PREFIX = "video:viewCount:";
    private static final String VIDEO_DATA_PATH = "/sample/video.json";

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private VideoService videoService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private ValueOperations<String, String> valueOps;

    @BeforeEach
    void setUp() throws IOException {
        valueOps = redisTemplate.opsForValue();

        InputStream inputStream = new ClassPathResource(VIDEO_DATA_PATH).getInputStream();
        List<Map<String, String>> videoSamples = objectMapper.readValue(inputStream, new TypeReference<>() {});

        if (videoSamples.size() != getAllVideos()) {
            saveVideosSampleData(videoSamples);
        }
    }

    private void saveVideosSampleData(List<Map<String, String>> videoSamples) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            videoSamples.forEach(video -> {
                String videoId = video.get("videoId");

                String key = VIDEO_KEY_PREFIX + videoId;

                connection.hMSet(key.getBytes(), video.entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                e -> e.getKey().getBytes(),
                                e -> e.getValue().getBytes()
                        )));
            });

            return null;
        });
    }

    private long getAllVideos() {
        return redisTemplate.getConnectionFactory().getConnection()
                .scan(ScanOptions.scanOptions().match(VIDEO_KEY_PREFIX + "*").count(20).build())
                .spliterator()
                .getExactSizeIfKnown();
    }


    @ParameterizedTest
    @ValueSource(strings = {"vid001", "vid002", "vid003"})
    void testIncrementViewCount(String videoId) {
        String key = VIDEO_VIEW_COUNT_PREFIX + videoId;

        String currentValue = valueOps.get(key);
        long expectedValue = (currentValue == null) ? 1 : Long.parseLong(currentValue) + 1;

        long updateCnt = videoService.incrementViewCount(key);

        assertThat(updateCnt).isEqualTo(expectedValue);

        updateCnt = videoService.incrementViewCount(key);
        expectedValue += 1;

        assertThat(updateCnt).isEqualTo(expectedValue);
    }
}
