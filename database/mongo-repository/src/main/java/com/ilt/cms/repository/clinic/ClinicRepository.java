package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.Status;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicRepository extends MongoRepository<Clinic, String> {

    @ExistsQuery(value = "{'clinicCode' : ?0 }")
    boolean checkClinicCodeExists(String clinicCode);

    Optional<Clinic> findClinicByClinicCode(String clinicCode);

    Optional<Clinic> findByIdAndStatus(String id, Status status);

    List<Clinic> findByIdIn(List<String> ids);
}
