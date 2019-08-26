package com.ilt.cms.database.coverage;

import com.ilt.cms.core.entity.coverage.PolicyHolderLimit;

public interface PolicyHolderLimitDatabaseService {
    PolicyHolderLimit findFirstByPatientId(String patientId);
}
