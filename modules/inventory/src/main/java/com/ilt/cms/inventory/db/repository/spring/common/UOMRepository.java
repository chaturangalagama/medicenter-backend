package com.ilt.cms.inventory.db.repository.spring.common;

import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.common.UOMMaster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UOMRepository extends MongoRepository<UOMMaster, String> {

     Optional<UOMMaster> findByCode(String code);
}
