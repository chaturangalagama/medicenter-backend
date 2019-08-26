package com.ilt.cms.repository.spring.policy;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyHolderInsuranceRepository extends MongoRepository<PolicyHolder.PolicyHolderInsurance, String> {

    List<PolicyHolder.PolicyHolderInsurance> findAllByIdentificationNumberAndStatus(UserId identificationNumber, Status status);

    @ExistsQuery("{'identificationNumber' : ?0, 'medicalCoverageId' : ?1, 'planId' : ?2 }")
    boolean checkIfUserHasCoverage(UserId userId, String medicalCoverage, String planId);

    @Query("{'identificationNumber' : ?0, 'planId' : ?1 }")
    PolicyHolder.PolicyHolderInsurance findIfUserHasCoverage(UserId userId, String planId);

    void deleteByIdAndMedicalCoverageIdAndPlanId(String userId, String medicalCoverage, String planId);

    Integer countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status);
}
