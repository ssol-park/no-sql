package com.psr.nosql;

import com.psr.nosql.service.EncryptService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/*
* @TestInstance(TestInstance.Lifecycle.PER_CLASS)
* 기본적으로 JUnit 5에서는 각 @Test 실행마다 새로운 테스트 인스턴스를 생성한다.
* => 각 테스트마다 EncryptTest 클래스의 새 객체가 만들어지고, 멤버 변수(keyMap)도 초기화됨
* => 해당 annotation 을 활용하면 테스트 클래스의 인스턴스가 한 번만 생성되고 모든 테스트에서 공유됨
* */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EncryptTest {

    @Autowired
    private EncryptService encryptService;

    private final Map<String, String> keyMap = new HashMap<>();

    @Order(1)
    @ParameterizedTest
    @CsvSource({
            "hello", "world", "java", "spring", "boot",
            "redis", "hashing", "encoding", "security", "test123"
    })
    void testSaveEncData(String origin) {
        String key = encryptService.saveAndReturnEncKey(origin, 180);

        keyMap.put(origin, key);
        assertThat(key.length()).isEqualTo(5);
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource({
            "hello", "world", "java", "spring", "boot",
            "redis", "hashing", "encoding", "security", "test123"
    })
    void testGetOriginByEncStr(String origin) {
        String encStr = keyMap.get(origin);

        String originStr = encryptService.getOriginByEncStr(encStr);

        assertThat(origin).isEqualTo(originStr);
    }
}
