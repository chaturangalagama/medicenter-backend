package com.ilt.cms.repository;

import com.ilt.cms.repository.spring.ClinicRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomClinicRepository {

    private final MongoTemplate mongoTemplate;
    private ClinicRepository clinicRepository;

    public CustomClinicRepository(MongoTemplate mongoTemplate, ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public ClinicRepository getClinicRepository() {
        return clinicRepository;
    }

}
