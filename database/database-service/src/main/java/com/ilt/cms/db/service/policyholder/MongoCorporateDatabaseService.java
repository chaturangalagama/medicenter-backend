package com.ilt.cms.db.service.policyholder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import com.ilt.cms.database.policyholder.CorporateDatabaseService;
import com.ilt.cms.repository.spring.policy.PolicyHolderCorporateRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MongoCorporateDatabaseService implements CorporateDatabaseService {

    private PolicyHolderCorporateRepository policyHolderCorporateRepository;
    private MongoTemplate mongoTemplate;

    public MongoCorporateDatabaseService(PolicyHolderCorporateRepository policyHolderCorporateRepository, MongoTemplate mongoTemplate){
        this.policyHolderCorporateRepository = policyHolderCorporateRepository;
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status) {
        return policyHolderCorporateRepository.countAllByMedicalCoverageIdAndStatus(medicalCoverageId, status);
    }

    @Override
    public PolicyHolder.PolicyHolderCorporate findIfUserHasCoverage(UserId holderId, String planId) {
        return policyHolderCorporateRepository.findIfUserHasCoverage(holderId, planId);
    }

    @Override
    public PolicyHolder.PolicyHolderCorporate findByIdentificationNumberAndRelationshipIsNull(UserId userId) {
        return policyHolderCorporateRepository.findByIdentificationNumberAndRelationshipIsNull(userId);
    }

    @Override
    public PolicyHolder.PolicyHolderCorporate save(PolicyHolder.PolicyHolderCorporate policyHolderCorporate) {
        return policyHolderCorporateRepository.save(policyHolderCorporate);
    }

    @Override
    public boolean delete(String holderId) {
        policyHolderCorporateRepository.deleteById(holderId);
        return true;
    }


    @Override
    public PolicyHolder.PolicyHolderCorporate findOne(String holderId) {
        Optional<PolicyHolder.PolicyHolderCorporate> policyHolderCorporateOpt = policyHolderCorporateRepository.findById(holderId);
        return policyHolderCorporateOpt.orElse(null);
    }

    @Override
    public List<PolicyHolder.PolicyHolderCorporate> findActivePolicyHolder(UserId identificationNumber, Status status,
                                                                           LocalDate startDate, LocalDate endDate) {
        Criteria criteria = Criteria.where("identificationNumber").is(identificationNumber)
                .and("status").is(status).and("startDate").lte(startDate)
                .orOperator(Criteria.where("endDate").gte(endDate), Criteria.where("endDate").exists(false));
        return mongoTemplate.find(Query.query(criteria), PolicyHolder.PolicyHolderCorporate.class);

    }

    @Override
    public boolean exists(PolicyHolder.PolicyHolderCorporate corporate) {
        return policyHolderCorporateRepository.exists(Example.of(corporate));
    }
}
