package com.ilt.cms.repository;

import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.repository.patient.PatientRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomPatientRepository {

    private PatientRepository patientRepository;
    private MongoTemplate mongoTemplate;

    public CustomPatientRepository(PatientRepository patientRepository, MongoTemplate mongoTemplate) {
        this.patientRepository = patientRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public boolean isUserIdAvailable(UserId userId) {
        return !mongoTemplate.exists(Query.query(
                Criteria.where("userId.idType").is(userId.getIdType())
                        .and("userId.number").is(userId.getNumber())), Patient.class);
    }


    public PatientRepository getPatientRepository() {
        return patientRepository;
    }
}


