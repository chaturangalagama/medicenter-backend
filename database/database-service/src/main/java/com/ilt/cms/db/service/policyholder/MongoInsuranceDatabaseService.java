package com.ilt.cms.db.service.policyholder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import com.ilt.cms.database.policyholder.InsuranceDatabaseService;
import com.ilt.cms.repository.spring.policy.PolicyHolderInsuranceRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoInsuranceDatabaseService implements InsuranceDatabaseService {

    private PolicyHolderInsuranceRepository policyHolderInsuranceRepository;

    public MongoInsuranceDatabaseService(PolicyHolderInsuranceRepository policyHolderInsuranceRepository){
        this.policyHolderInsuranceRepository = policyHolderInsuranceRepository;
    }
    @Override
    public int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status) {
        return policyHolderInsuranceRepository.countAllByMedicalCoverageIdAndStatus(medicalCoverageId, status);
    }

    @Override
    public PolicyHolder.PolicyHolderInsurance findIfUserHasCoverage(UserId userId, String planId) {
        return policyHolderInsuranceRepository.findIfUserHasCoverage(userId, planId);
    }

    @Override
    public boolean checkIfUserHasCoverage(UserId identificationNumber, String medicalCoverageId, String planId) {
        return policyHolderInsuranceRepository.checkIfUserHasCoverage(identificationNumber, medicalCoverageId, planId);
    }

    @Override
    public PolicyHolder.PolicyHolderInsurance save(PolicyHolder.PolicyHolderInsurance policyHolderInsurance) {
        return policyHolderInsuranceRepository.save(policyHolderInsurance);
    }

    @Override
    public boolean deleteByIdAndMedicalCoverageIdAndPlanId(String holderId, String medicalCoverageId, String planId) {
        policyHolderInsuranceRepository.deleteByIdAndMedicalCoverageIdAndPlanId(holderId, medicalCoverageId, planId);
        return true;
    }

    @Override
    public PolicyHolder.PolicyHolderInsurance findOne(String holderId) {
        return policyHolderInsuranceRepository.findById(holderId).orElse(null);
    }

    @Override
    public boolean exists(PolicyHolder.PolicyHolderInsurance insurance) {
        return policyHolderInsuranceRepository.exists(Example.of(insurance));
    }

    @Override
    public List<PolicyHolder.PolicyHolderInsurance> findAllByIdentificationNumberAndStatus(UserId userId, Status status) {
        return policyHolderInsuranceRepository.findAllByIdentificationNumberAndStatus(userId, status);
    }
}
