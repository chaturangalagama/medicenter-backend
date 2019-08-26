package com.ilt.cms.db.service.policyholder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import com.ilt.cms.database.policyholder.*;
import com.ilt.cms.repository.PolicyHolderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MongoPolicyHolderDatabaseService implements PolicyHolderDatabaseService {

    private PolicyHolderRepository policyHolderRepository;
    private ChasDatabaseService chasDatabaseService;
    private CorporateDatabaseService corporateDatabaseService;
    private InsuranceDatabaseService insuranceDatabaseService;
    private MediSaveDatabaseService mediSaveDatabaseService;

    public MongoPolicyHolderDatabaseService(PolicyHolderRepository policyHolderRepository, ChasDatabaseService chasDatabaseService,
                                            CorporateDatabaseService corporateDatabaseService, InsuranceDatabaseService insuranceDatabaseService,
                                            MediSaveDatabaseService mediSaveDatabaseService){
        this.policyHolderRepository = policyHolderRepository;
        this.chasDatabaseService = chasDatabaseService;
        this.corporateDatabaseService = corporateDatabaseService;
        this.insuranceDatabaseService = insuranceDatabaseService;
        this.mediSaveDatabaseService = mediSaveDatabaseService;
    }

    @Override
    public int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status) {
        return 0;
    }

    @Override
    public boolean isUserAssociatedWithPlan(UserId userId, String medicalCoverageId, String planId) {
        return policyHolderRepository.isUserAssociatedWithPlan(userId, medicalCoverageId, planId);
    }

    public ChasDatabaseService getChasDatabaseService() {
        return chasDatabaseService;
    }

    public void setChasDatabaseService(ChasDatabaseService chasDatabaseService) {
        this.chasDatabaseService = chasDatabaseService;
    }

    public CorporateDatabaseService getCorporateDatabaseService() {
        return corporateDatabaseService;
    }

    public void setCorporateDatabaseService(CorporateDatabaseService corporateDatabaseService) {
        this.corporateDatabaseService = corporateDatabaseService;
    }

    public InsuranceDatabaseService getInsuranceDatabaseService() {
        return insuranceDatabaseService;
    }

    public void setInsuranceDatabaseService(InsuranceDatabaseService insuranceDatabaseService) {
        this.insuranceDatabaseService = insuranceDatabaseService;
    }

    public MediSaveDatabaseService getMediSaveDatabaseService() {
        return mediSaveDatabaseService;
    }

    public void setMediSaveDatabaseService(MediSaveDatabaseService mediSaveDatabaseService) {
        this.mediSaveDatabaseService = mediSaveDatabaseService;
    }

    @Override
    public List<PolicyHolder.PolicyHolderCorporate> findActivePolicyHolder(UserId userId, Status active, LocalDate localDate, LocalDate localDate1) {
        return corporateDatabaseService.findActivePolicyHolder(userId, active, localDate, localDate1);
    }

    @Override
    public boolean isMedicalCoverageUsed(String medicalCoverageId) {
        PolicyHolder.PolicyHolderChas chas = new PolicyHolder.PolicyHolderChas();
        chas.setMedicalCoverageId(medicalCoverageId);

        PolicyHolder.PolicyHolderCorporate corporate = new PolicyHolder.PolicyHolderCorporate();
        corporate.setMedicalCoverageId(medicalCoverageId);

        PolicyHolder.PolicyHolderInsurance insurance = new PolicyHolder.PolicyHolderInsurance();
        insurance.setMedicalCoverageId(medicalCoverageId);

        PolicyHolder.PolicyHolderMediSave mediSave = new PolicyHolder.PolicyHolderMediSave();
        mediSave.setMedicalCoverageId(medicalCoverageId);

        return chasDatabaseService.exists(chas) || corporateDatabaseService.exists(corporate)
                || insuranceDatabaseService.exists(insurance) || mediSaveDatabaseService.exists(mediSave);
    }

    @Override
    public boolean isPlanUsed(String medicalCoverageId, String planId) {
        PolicyHolder.PolicyHolderChas chas = new PolicyHolder.PolicyHolderChas();
        chas.setMedicalCoverageId(medicalCoverageId);
        chas.setPlanId(planId);

        PolicyHolder.PolicyHolderCorporate corporate = new PolicyHolder.PolicyHolderCorporate();
        corporate.setMedicalCoverageId(medicalCoverageId);
        corporate.setPlanId(planId);

        PolicyHolder.PolicyHolderInsurance insurance = new PolicyHolder.PolicyHolderInsurance();
        insurance.setMedicalCoverageId(medicalCoverageId);
        insurance.setPlanId(planId);

        PolicyHolder.PolicyHolderMediSave mediSave = new PolicyHolder.PolicyHolderMediSave();
        mediSave.setMedicalCoverageId(medicalCoverageId);
        mediSave.setPlanId(planId);

        return chasDatabaseService.exists(chas) || corporateDatabaseService.exists(corporate)
                || insuranceDatabaseService.exists(insurance) || mediSaveDatabaseService.exists(mediSave);
    }


}
