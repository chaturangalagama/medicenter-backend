package com.ilt.cms.repository.patient.patientVisit;

import com.ilt.cms.core.entity.diagnosis.Diagnosis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends MongoRepository<Diagnosis, String> {


    @Query("{ '$and' : [ {'status' : 'ACTIVE'}, " +
            "{ '$or' : [ {'icd10Code' : { $regex: ?0, $options: 'i'}}, {'icd10Term' : { $regex: ?0, $options: 'i'}}, " +
            "{'snomedCode' : { $regex: ?0, $options: 'i'}}, {'snomedTerm' : { $regex: ?0, $options: 'i'}} ]  " +
            "}] } ")
    List<Diagnosis> search(String term);

    @Query("{ '$and' : [ {'status' : 'ACTIVE'}, " +
            "{ '$or' : [ {'icd10Code' : { $regex: ?0, $options: 'i'}}, {'icd10Term' : { $regex: ?0, $options: 'i'}}, " +
            "{'snomedCode' : { $regex: ?0, $options: 'i'}}, {'snomedTerm' : { $regex: ?0, $options: 'i'}} ]}" +
            ", {'filterablePlanIds' : {$elemMatch: {$in : ?1} } }] } ")
    List<Diagnosis> searchFilerByPlan(String term, List<String> planIds);

}
