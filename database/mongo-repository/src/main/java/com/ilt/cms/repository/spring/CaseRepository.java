package com.ilt.cms.repository.spring;

import com.ilt.cms.core.entity.casem.Case;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends MongoRepository<Case, String>, CaseRepositoryCustom {

    boolean existsByIdAndStatus(String id, Case.CaseStatus status);

    List<Case> findByClinicId(String clinicId);

    List<Case> findByClinicId(String clinicId, Pageable pageable);

    List<Case> findCasesByPatientId(String patientId);

    Case findByVisitIdsContains(String visitId);
}
