package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.allergy.AllergyGroup;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergyGroupRepository extends MongoRepository<AllergyGroup, String> {

    AllergyGroup findFirstByGroupCode(String groupCode);

    @ExistsQuery(value = "{'groupCode' : ?0 }")
    boolean allergyCodeExists(String groupCode);

    @ExistsQuery(value = "{'groupCode' : ?0, '_id' : {'$ne' : ?1} }")
    boolean allergyCodeExists(String groupCode, String excludeValidationCode);

}
