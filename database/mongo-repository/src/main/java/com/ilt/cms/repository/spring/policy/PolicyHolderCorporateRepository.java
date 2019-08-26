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
public interface PolicyHolderCorporateRepository extends MongoRepository<PolicyHolder.PolicyHolderCorporate, String> {


    @ExistsQuery("{'identificationNumber' : ?0, 'medicalCoverageId' : ?1, 'planId' : ?2 }")
    boolean findIfUserHasCoverage(UserId userId, String medicalCoverage, String planId);

    List<PolicyHolder.PolicyHolderCorporate> findAllByIdentificationNumber(UserId identificationNumber);

    @Query("{'identificationNumber' : ?0, 'planId' : ?1 }")
    PolicyHolder.PolicyHolderCorporate findIfUserHasCoverage(UserId userId, String planId);


    PolicyHolder.PolicyHolderCorporate findByIdentificationNumberAndRelationshipIsNull(UserId userId);

    //todo localdate does not seems to play well with mongodb, need to relook at this
//    @Query("{ 'identificationNumber' : ?0, 'status' : ?1, 'startDate' : { $lte : ?2 }, " +
//            "$or : [{ 'endDate' : {$gte : ?3}, {'endDate' : null}] }")
//    List<PolicyHolder.PolicyHolderCorporate> findActivePolicyHolder(UserId identificationNumber, Status status,
//                                                                    LocalDate startDate, LocalDate endDate);

    int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status);

}
