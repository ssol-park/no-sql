package com.psr.nosql.repository;

import com.psr.nosql.entity.Name;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends CrudRepository<Name, String> {
}
