package com.psr.nosql.repository;


import com.psr.nosql.entity.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChannelRepository extends MongoRepository<Channel, String> {
}
