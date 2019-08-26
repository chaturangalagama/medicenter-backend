package com.ilt.cms.repository;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import com.ilt.cms.repository.spring.policy.PolicyHolderChasRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderCorporateRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderInsuranceRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderMediSaveRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PolicyHolderRepository {

    private final MongoTemplate mongoTemplate;
    private final PolicyHolderChasRepository chasRepository;
    private final PolicyHolderCorporateRepository corporateRepository;
    private final PolicyHolderInsuranceRepository insuranceRepository;
    private final PolicyHolderMediSaveRepository mediSaveRepository;


    public PolicyHolderRepository(MongoTemplate mongoTemplate, PolicyHolderChasRepository chasRepository,
                                  PolicyHolderCorporateRepository corporateRepository,
                                  PolicyHolderInsuranceRepository insuranceRepository,
                                  PolicyHolderMediSaveRepository mediSaveRepository) {
        this.mongoTemplate = mongoTemplate;
        this.chasRepository = chasRepository;
        this.corporateRepository = corporateRepository;
        this.insuranceRepository = insuranceRepository;
        this.mediSaveRepository = mediSaveRepository;
    }

    public boolean isMedicalCoverageUsed(String medicalCoverageId) {
        PolicyHolder.PolicyHolderChas chas = new PolicyHolder.PolicyHolderChas();
        chas.setMedicalCoverageId(medicalCoverageId);

        PolicyHolder.PolicyHolderCorporate corporate = new PolicyHolder.PolicyHolderCorporate();
        corporate.setMedicalCoverageId(medicalCoverageId);

        PolicyHolder.PolicyHolderInsurance insurance = new PolicyHolder.PolicyHolderInsurance();
        insurance.setMedicalCoverageId(medicalCoverageId);

        PolicyHolder.PolicyHolderMediSave mediSave = new PolicyHolder.PolicyHolderMediSave();
        mediSave.setMedicalCoverageId(medicalCoverageId);

        return chasRepository.exists(Example.of(chas)) || corporateRepository.exists(Example.of(corporate))
                || insuranceRepository.exists(Example.of(insurance)) || mediSaveRepository.exists(Example.of(mediSave));
    }

    public boolean isUserAssociatedWithPlan(UserId userId, String medicalId, String planId) {
        return corporateRepository.findIfUserHasCoverage(userId, medicalId, planId)
                || mediSaveRepository.findIfUserHasCoverage(userId, medicalId, planId)
                || chasRepository.findIfUserHasCoverage(userId, medicalId, planId)
                || insuranceRepository.checkIfUserHasCoverage(userId, medicalId, planId);
    }

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

        return chasRepository.exists(Example.of(chas)) || corporateRepository.exists(Example.of(corporate))
                || insuranceRepository.exists(Example.of(insurance)) || mediSaveRepository.exists(Example.of(mediSave));
    }

    public PolicyHolderChasRepository getChasRepository() {
        return chasRepository;
    }

    public PolicyHolderCorporateRepository getCorporateRepository() {
        return corporateRepository;
    }

    /*public List<PolicyHolder.PolicyHolderCorporate> findActivePolicyHolder(UserId identificationNumber, Status status,
                                                                    LocalDate startDate, LocalDate endDate) {
        Criteria criteria = Criteria.where("identificationNumber").is(identificationNumber)
                .and("status").is(status).and("startDate").lte(startDate)
                .orOperator(Criteria.where("endDate").gte(endDate), Criteria.where("endDate").exists(false));
        return mongoTemplate.find(Query.query(criteria), PolicyHolder.PolicyHolderCorporate.class);

    }*/

    public PolicyHolderInsuranceRepository getInsuranceRepository() {
        return insuranceRepository;
    }

    public PolicyHolderMediSaveRepository getMediSaveRepository() {
        return mediSaveRepository;
    }
}
