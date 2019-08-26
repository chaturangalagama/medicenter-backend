package com.ilt.cms.repository.spring;

import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.patient.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

    Optional<Patient> findByUserId(UserId userId);

    @Query("{'userId.number' : ?0}")
    Patient findByUserId(String userId);

    boolean existsByUserId(UserId userId);

    List<Patient> findAllByNameLike(String name);

    @ExistsQuery(value = "{ 'coverages.medicalCoverageId' : ?0 }")
    boolean isMedicalServiceIdUsed(String medicalCoverageId);

    @ExistsQuery(value = "{ 'coverages.medicalCoverageId' : ?0, 'coverages.planId' : ?1}")
    boolean isMedicalPlanIdUsed(String medicalCoverageId, String planId);

    @Query("{ $text: {$search: ?0 } }")
    List<Patient> patientLikeSearch(String value, Pageable pageable);

    List<Patient> findByIdIn(List<String> patientIds);
}