package com.ilt.cms.database.vaccination;

import com.ilt.cms.core.entity.vaccination.AssociatedCoverageVaccination;

public interface AssociatedCoverageVaccinationDatabaseService {
    boolean deleteByIdAndMedicalCoverageId(String associationId, String medicalCoverageId);

    AssociatedCoverageVaccination findByMedicalCoverageIdAndCoveragePlanId(String medicalCoverageId, String planId);

    boolean deleteByMedicalCoverageIdAndCoveragePlanId(String medicalCoverageId, String coverageId);

    AssociatedCoverageVaccination save(AssociatedCoverageVaccination associatedCoverageVaccination);
}
