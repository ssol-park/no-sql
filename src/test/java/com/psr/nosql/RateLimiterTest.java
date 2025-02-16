package com.psr.nosql;

import com.psr.nosql.util.RateLimiter;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RateLimiterTest {

    @Autowired
    private RateLimiter rateLimiter;

    @ParameterizedTest
    @ValueSource(strings = {"user_1234"})
    void 분당_10회_요청_허용 (String userId) {
        rateLimiter.reset(userId); // 테스트 시작 전 초기화

        for (int i = 0; i < 10; i++) {
            assertThat(rateLimiter.isAllowed(userId)).isTrue();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"user_1234"})
    void 분당_11회_요청 (String userId) throws InterruptedException {
        rateLimiter.reset(userId); // 테스트 시작 전 초기화

        for (int i = 0; i < 10; i++) {
            Thread.sleep(100);
            assertThat(rateLimiter.isAllowed(userId)).isTrue();
        }

        assertThat(rateLimiter.isAllowed(userId)).isTrue();
    }
}
