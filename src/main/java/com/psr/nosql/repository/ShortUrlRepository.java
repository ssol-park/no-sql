package com.psr.nosql.repository;

import com.psr.nosql.entity.ShortUrlDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShortUrlRepository extends MongoRepository<ShortUrlDocument, String> {
}
