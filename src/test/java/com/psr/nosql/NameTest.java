package com.psr.nosql;

import com.psr.nosql.constant.MessageConstant;
import com.psr.nosql.dto.NameDto;
import com.psr.nosql.service.NameService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NameTest {

    @Autowired
    private NameService nameService;

    @Order(1)
    @ParameterizedTest
    @CsvSource({
            "John, 존", "Alice, 앨리스", "Bob, 밥", "Charlie, 찰리",
            "David, 데이비드", "Emily, 에밀리", "Frank, 프랭크", "Grace, 그레이스",
            "Henry, 헨리", "Ivy, 아이비"
    })
    void testSaveName(String engName, String korName) {
        NameDto nameDto = NameDto.builder()
                .engName(engName)
                .korName(korName)
                .build();

        nameService.saveName(nameDto);

        String storedKorName = nameService.getKorName(engName);

        assertThat(storedKorName).isEqualTo(korName);
    }

    @Order(2)
    @ParameterizedTest
    @ValueSource(strings = "John")
    void testTTLExpires(String engName) throws InterruptedException {
        Thread.sleep(11000);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
           nameService.getKorName(engName)
        );

        assertThat(ex.getMessage()).isEqualTo(MessageConstant.NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = "John")
    void testGetKorName(String engName) {
        String storedKorName = nameService.getKorName(engName);

        assertThat(storedKorName).isEmpty();
    }
}
