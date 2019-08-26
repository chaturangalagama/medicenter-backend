package com.ilt.cms.repository.spring;

import com.ilt.cms.core.entity.consultation.MedicalServiceGiven;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalServiceGivenRepository extends MongoRepository<MedicalServiceGiven, String> {

}
