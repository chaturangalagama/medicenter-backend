package com.ilt.cms.repository.spring.policy;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyHolderMediSaveRepository extends MongoRepository<PolicyHolder.PolicyHolderMediSave, String> {

    @ExistsQuery("{'identificationNumber' : ?0, 'medicalCoverageId' : ?1, 'planId' : ?2 }")
    boolean findIfUserHasCoverage(UserId userId, String medicalCoverage, String planId);

    List<PolicyHolder.PolicyHolderMediSave> findAllByIdentificationNumberAndStatus(UserId identificationNumber, Status status);

    List<PolicyHolder.PolicyHolderMediSave> findByIdentificationNumber(UserId identificationNumber);

    Integer countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status);
}
