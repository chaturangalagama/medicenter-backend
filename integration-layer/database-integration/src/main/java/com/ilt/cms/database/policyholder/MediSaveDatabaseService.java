package com.ilt.cms.database.policyholder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;

import java.util.List;

public interface MediSaveDatabaseService {
    int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status);

    List<PolicyHolder.PolicyHolderMediSave> findByIdentificationNumber(UserId IdentificationNumber);

    PolicyHolder.PolicyHolderMediSave save(PolicyHolder.PolicyHolderMediSave policyHolderMediSave);

    boolean delete(String holderId);

    PolicyHolder.PolicyHolderMediSave findOne(String holderId);

    List<PolicyHolder.PolicyHolderMediSave> findAllByIdentificationNumberAndStatus(UserId userId, Status status);

    boolean exists(PolicyHolder.PolicyHolderMediSave mediSave);
}
