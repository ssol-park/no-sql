package com.psr.nosql.repository;

import com.psr.nosql.entity.NameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends CrudRepository<NameEntity, String> {
}
