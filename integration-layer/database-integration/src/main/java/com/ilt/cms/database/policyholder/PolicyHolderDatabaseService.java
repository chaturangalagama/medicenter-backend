package com.ilt.cms.database.policyholder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;

import java.time.LocalDate;
import java.util.List;

public interface PolicyHolderDatabaseService {

    int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status);

    boolean isUserAssociatedWithPlan(UserId userId, String medicalCoverageId, String planId);

    ChasDatabaseService getChasDatabaseService();

    void setChasDatabaseService(ChasDatabaseService chasDatabaseService);

    CorporateDatabaseService getCorporateDatabaseService();

    void setCorporateDatabaseService(CorporateDatabaseService corporateDatabaseService);

    InsuranceDatabaseService getInsuranceDatabaseService();

    void setInsuranceDatabaseService(InsuranceDatabaseService insuranceDatabaseService);

    MediSaveDatabaseService getMediSaveDatabaseService();

    void setMediSaveDatabaseService(MediSaveDatabaseService mediSaveDatabaseService);

    List<PolicyHolder.PolicyHolderCorporate> findActivePolicyHolder(UserId userId, Status active, LocalDate localDate, LocalDate localDate1);

    boolean isMedicalCoverageUsed(String medicalCoverageId);

    boolean isPlanUsed(String medicalCoverageId, String planId);
}
