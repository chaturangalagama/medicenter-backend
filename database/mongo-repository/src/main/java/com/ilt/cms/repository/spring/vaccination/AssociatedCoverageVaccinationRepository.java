package com.ilt.cms.repository.spring.vaccination;

import com.ilt.cms.core.entity.vaccination.AssociatedCoverageVaccination;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociatedCoverageVaccinationRepository extends MongoRepository<AssociatedCoverageVaccination, String> {

    void deleteByIdAndMedicalCoverageId(String associationId, String medicalCoverageId);

    AssociatedCoverageVaccination findByMedicalCoverageIdAndCoveragePlanId(String medicalCoverageId, String planId);

    void deleteByMedicalCoverageIdAndCoveragePlanId(String medicalCoverageId, String coverageId);

}
