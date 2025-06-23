package com.psr.nosql.service;

import com.psr.nosql.cache.RedisShortUrlCache;
import com.psr.nosql.config.BaseProperties;
import com.psr.nosql.dto.ShortUrlResponse;
import com.psr.nosql.dto.UrlRequestDto;
import com.psr.nosql.entity.ShortUrlDocument;
import com.psr.nosql.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@SpringBootTest
class UrlServiceTest {

    // 테스트 대상 및 의존성
    private ShortUrlRepository shortUrlRepo;
    private RedisShortUrlCache redisCache;
    private ShortCodeGenerator codeGenerator;
    private BaseProperties baseProperties;
    private UrlService urlService;

    // 테스트 픽스처(공통 데이터)
    private final String originalUrl = "https://example.com";
    private final String baseUrl = "https://short.ly";
    private final String shortCode = "aB12Cd";
    private final Duration defaultExpiration = Duration.ofSeconds(3600);

    @BeforeEach
    void setUp() {
        // 각 테스트 전에 Mock 객체 생성 - 테스트 격리 보장
        shortUrlRepo = mock(ShortUrlRepository.class);
        redisCache = mock(RedisShortUrlCache.class);
        codeGenerator = mock(ShortCodeGenerator.class);
        baseProperties = mock(BaseProperties.class);

        // 테스트 대상 객체 생성
        urlService = new UrlService(shortUrlRepo, redisCache, codeGenerator, baseProperties);

        // 기본 스텁 설정
        when(baseProperties.getUrl()).thenReturn(baseUrl);
    }

    @Test
    void 새로운_URL을_등록하면_단축URL이_생성되어야한다() {
        // Arrange (Given) - 테스트 사전 조건 설정
        UrlRequestDto requestDto = new UrlRequestDto(originalUrl);

        when(codeGenerator.generate(originalUrl, "")).thenReturn(shortCode);
        when(redisCache.get(shortCode)).thenReturn(null);
        when(shortUrlRepo.findById(shortCode)).thenReturn(Optional.empty());
        when(shortUrlRepo.existsById(shortCode)).thenReturn(false);

        LocalDateTime now = LocalDateTime.now();
        ShortUrlDocument savedDocument = ShortUrlDocument.builder()
                .shortCode(shortCode)
                .originalUrl(originalUrl)
                .createdAt(now)
                .expiresAt(now.plus(defaultExpiration))
                .build();

        when(shortUrlRepo.save(any(ShortUrlDocument.class))).thenReturn(savedDocument);

        // Act (When) - 테스트 대상 메서드 실행
        ShortUrlResponse response = urlService.createShortUrl(requestDto);

        // Assert (Then) - 결과 검증
        assertThat(response.getShortUrl())
                .as("생성된 단축 URL이 예상 형식과 일치해야 함")
                .isEqualTo(baseUrl + "/" + shortCode);

        verify(shortUrlRepo).save(any(ShortUrlDocument.class));
        verify(redisCache).save(shortCode, originalUrl, defaultExpiration);
    }

    @Test
    void 캐시에_이미_존재하는_URL을_등록하면_DB조회없이_기존_단축URL을_반환해야한다() {
        // Arrange
        UrlRequestDto requestDto = new UrlRequestDto(originalUrl);

        when(codeGenerator.generate(originalUrl, "")).thenReturn(shortCode);
        when(redisCache.get(shortCode)).thenReturn(originalUrl);

        // Act
        ShortUrlResponse response = urlService.createShortUrl(requestDto);

        // Assert
        assertThat(response.getShortUrl())
                .as("캐시에서 가져온 단축 URL이 예상대로 반환되어야 함")
                .isEqualTo(baseUrl + "/" + shortCode);

        verifyNoInteractions(shortUrlRepo);
    }

    @Test
    void DB에_이미_존재하는_URL을_등록하면_캐시_저장없이_기존_단축URL을_반환해야한다() {
        // Arrange
        UrlRequestDto requestDto = new UrlRequestDto(originalUrl);

        when(codeGenerator.generate(originalUrl, "")).thenReturn(shortCode);
        when(redisCache.get(shortCode)).thenReturn(null);

        ShortUrlDocument existingDocument = ShortUrlDocument.builder()
                .shortCode(shortCode)
                .originalUrl(originalUrl)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plus(defaultExpiration))
                .build();

        when(shortUrlRepo.findById(shortCode)).thenReturn(Optional.of(existingDocument));

        // Act
        ShortUrlResponse response = urlService.createShortUrl(requestDto);

        // Assert
        assertThat(response.getShortUrl())
                .as("DB에서 가져온 단축 URL이 예상대로 반환되어야 함")
                .isEqualTo(baseUrl + "/" + shortCode);

        verify(shortUrlRepo, never()).save(any(ShortUrlDocument.class));
        verify(redisCache, never()).save(anyString(), anyString(), any(Duration.class));
    }

    @Test
    void 단축코드_충돌이_최대치_이상_발생하면_예외가_발생해야한다() {
        // Arrange
        UrlRequestDto requestDto = new UrlRequestDto(originalUrl);

        when(codeGenerator.generate(eq(originalUrl), anyString())).thenReturn(shortCode);
        when(redisCache.get(shortCode)).thenReturn(null);
        when(shortUrlRepo.findById(shortCode)).thenReturn(Optional.empty());
        when(shortUrlRepo.existsById(shortCode)).thenReturn(true);

        // Act & Assert
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> urlService.createShortUrl(requestDto))
                .withMessageMatching(".*(?:충돌|conflict).*");

    }
}