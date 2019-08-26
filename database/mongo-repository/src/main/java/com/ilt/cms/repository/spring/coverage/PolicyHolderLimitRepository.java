package com.ilt.cms.repository.spring.coverage;

import com.ilt.cms.core.entity.coverage.PolicyHolderLimit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyHolderLimitRepository extends MongoRepository<PolicyHolderLimit, String> {

    PolicyHolderLimit findFirstByPatientId(String patientId);
}
