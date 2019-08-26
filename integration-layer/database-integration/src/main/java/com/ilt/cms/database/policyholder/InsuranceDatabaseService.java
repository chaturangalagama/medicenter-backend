package com.ilt.cms.database.policyholder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;

import java.util.List;

public interface InsuranceDatabaseService {
    int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status);

    PolicyHolder.PolicyHolderInsurance findIfUserHasCoverage(UserId userId, String planId);

    List<PolicyHolder.PolicyHolderInsurance> findAllByIdentificationNumberAndStatus(UserId userId, Status status);

    boolean checkIfUserHasCoverage(UserId IdentificationNumber, String MedicalCoverageId, String planId);

    PolicyHolder.PolicyHolderInsurance save(PolicyHolder.PolicyHolderInsurance PolicyHolderInsurance);

    boolean deleteByIdAndMedicalCoverageIdAndPlanId(String holderId, String medicalCoverageId, String planId);

    PolicyHolder.PolicyHolderInsurance findOne(String holderId);

    boolean exists(PolicyHolder.PolicyHolderInsurance insurance);

}
