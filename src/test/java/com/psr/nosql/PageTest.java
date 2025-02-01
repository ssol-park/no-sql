package com.psr.nosql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.psr.nosql.entity.Page;
import com.psr.nosql.repository.PageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PageTest {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void insertPagesFromJson() throws IOException {
        var resource = new ClassPathResource("/static/url.json");
        List<Page> pages = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});

        pageRepository.saveAll(pages);

        assertThat(pages.size()).isEqualTo(pageRepository.findAll().size());
    }
}
