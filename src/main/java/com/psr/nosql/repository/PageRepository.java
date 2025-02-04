package com.psr.nosql.repository;

import com.psr.nosql.entity.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PageRepository extends MongoRepository<Page, String> {
}
