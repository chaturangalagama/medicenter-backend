package com.ilt.cms.repository;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomMedicalCoverageRepository {

    private MedicalCoverageRepository medicalCoverageRepository;

    private MongoTemplate mongoTemplate;

    public CustomMedicalCoverageRepository(MedicalCoverageRepository medicalCoverageRepository,
                                           MongoTemplate mongoTemplate) {
        this.medicalCoverageRepository = medicalCoverageRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public MedicalCoverageRepository getMedicalCoverageRepository() {
        return medicalCoverageRepository;
    }

    //it looks like Spring does not return sub objects, unless a new feature comes in we need to this manually
    public CoveragePlan findPlan(String medicalCoverageId, String planId) {
        MedicalCoverage medicalCoverage = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(medicalCoverageId)
                .and("coveragePlans._id").is(planId)), MedicalCoverage.class);
        return MedicalCoverage.findPlan(medicalCoverage, planId);
    }

    public CoveragePlan findPlan(String planId) {
        return MedicalCoverage.findPlan(mongoTemplate.findOne(Query.query(Criteria.where("coveragePlans._id")
                .is(planId)), MedicalCoverage.class), planId);
    }

    public CoveragePlan findActivePlan(String planId) {
        return MedicalCoverage.findPlan(mongoTemplate.findOne(
                Query.query(Criteria.where("coveragePlans._id").is(planId)
                        .and("status").is(Status.ACTIVE.toString())
                        .and("coveragePlans.status").is(Status.ACTIVE.toString())), MedicalCoverage.class), planId);
    }

    public MedicalCoverage findPlanByMedicalCoverageId(String medicalCoverageId) {
        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(medicalCoverageId)), MedicalCoverage.class);
    }

    public boolean doesPlanExists(String medicalCoverageId, String planId) {
        return mongoTemplate.exists(Query.query(Criteria.where("_id").is(medicalCoverageId)
                .and("coveragePlans._id").is(planId)), MedicalCoverage.class);
    }

    public boolean doesCoverageNameExists(String coverageName) {
        return mongoTemplate.exists(Query.query(Criteria.where("name").is(coverageName)), MedicalCoverage.class);
    }

    public MedicalCoverage findMedicalCoverageByPlan(String planId) {
        return mongoTemplate.findOne(Query.query(Criteria.where("coveragePlans._id")
                .is(planId)), MedicalCoverage.class);
    }


}