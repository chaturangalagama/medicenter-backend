package com.ilt.cms.database.policyholder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;

import java.util.List;

public interface ChasDatabaseService {
    int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status);

    List<PolicyHolder.PolicyHolderChas> findByIdentificationNumber(UserId IdentificationNumber);

    PolicyHolder.PolicyHolderChas save(PolicyHolder.PolicyHolderChas policyHolderChas);

    boolean delete(String holderId);

    PolicyHolder.PolicyHolderChas findOne(String HolderId);

    List<PolicyHolder.PolicyHolderChas> findAllByIdentificationNumberAndStatus(UserId identificationNumber, Status status);

    boolean exists(PolicyHolder.PolicyHolderChas chas);
}
