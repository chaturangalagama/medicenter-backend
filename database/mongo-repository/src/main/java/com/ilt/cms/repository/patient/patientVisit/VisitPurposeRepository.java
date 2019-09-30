package com.ilt.cms.repository.patient.patientVisit;

import com.ilt.cms.core.entity.visit.VisitPurpose;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitPurposeRepository extends MongoRepository<VisitPurpose, String> {

    @ExistsQuery(value = "{'name' : ?0 }")
    boolean checkIfNameExists(String name);

    @ExistsQuery("{ '_id' : {$ne : ?0} }, 'name' : ?1}")
    boolean checkIfNameExistsNotTheSameId(String name, String currentId);

    Optional<VisitPurpose> findByName(String name);
}
