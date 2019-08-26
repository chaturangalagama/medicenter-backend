package com.ilt.cms.database.policyholder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;

import java.time.LocalDate;
import java.util.List;

public interface CorporateDatabaseService {
    int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status);

    PolicyHolder.PolicyHolderCorporate findIfUserHasCoverage(UserId holderId, String planId);

    PolicyHolder.PolicyHolderCorporate findByIdentificationNumberAndRelationshipIsNull(UserId userId);

    PolicyHolder.PolicyHolderCorporate save(PolicyHolder.PolicyHolderCorporate policyHolderCorporate);

    boolean delete(String holderId);

    PolicyHolder.PolicyHolderCorporate findOne(String holderId);

    List<PolicyHolder.PolicyHolderCorporate> findActivePolicyHolder(UserId userId, Status statue, LocalDate startDate, LocalDate endDate);

    boolean exists(PolicyHolder.PolicyHolderCorporate corporate);
}
