package com.psr.nosql;

import com.psr.nosql.config.MongoDBConfig;
import com.psr.nosql.entity.Channel;
import com.psr.nosql.repository.ChannelRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(MongoDBConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChannelTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeAll
    public void setUp() {
        mongoTemplate.dropCollection(Channel.class);  // channels 컬렉션 초기화
    }

    @ParameterizedTest
    @ValueSource(strings = {"TestChannel1", "TestChannel2", "TestChannel3"})
    void testCreateChannel(String channelName) {

        Channel newChannel = Channel.builder()
                .channelName(channelName)
                .visitorCount(0)
                .build();

        Channel savedChannel = channelRepository.save(newChannel);

        assertThat(savedChannel).isNotNull();
        assertThat(savedChannel.getChannelName()).isEqualTo(channelName);
        assertThat(savedChannel.getVisitorCount()).isEqualTo(0);
    }
}
