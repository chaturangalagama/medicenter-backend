package com.ilt.cms.inventory.db.repository.spring.common;

import com.ilt.cms.inventory.model.common.UomMatrix;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UOMMatrixRepository extends MongoRepository<UomMatrix, String> {

    Optional<UomMatrix> findByUomCode(String sourceUomCode);

}
