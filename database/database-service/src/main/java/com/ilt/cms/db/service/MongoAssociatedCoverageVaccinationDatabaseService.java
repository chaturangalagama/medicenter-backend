package com.ilt.cms.db.service;

import com.ilt.cms.core.entity.vaccination.AssociatedCoverageVaccination;
import com.ilt.cms.database.vaccination.AssociatedCoverageVaccinationDatabaseService;
import com.ilt.cms.repository.spring.vaccination.AssociatedCoverageVaccinationRepository;
import org.springframework.stereotype.Service;

@Service
public class MongoAssociatedCoverageVaccinationDatabaseService implements AssociatedCoverageVaccinationDatabaseService {

    private AssociatedCoverageVaccinationRepository associatedCoverageVaccinationRepository;

    public MongoAssociatedCoverageVaccinationDatabaseService(AssociatedCoverageVaccinationRepository associatedCoverageVaccinationRepository){
        this.associatedCoverageVaccinationRepository = associatedCoverageVaccinationRepository;
    }

    @Override
    public boolean deleteByIdAndMedicalCoverageId(String associationId, String medicalCoverageId) {
        associatedCoverageVaccinationRepository.deleteByIdAndMedicalCoverageId(associationId, medicalCoverageId);
        return true;
    }

    @Override
    public AssociatedCoverageVaccination findByMedicalCoverageIdAndCoveragePlanId(String medicalCoverageId, String planId) {
        return associatedCoverageVaccinationRepository.findByMedicalCoverageIdAndCoveragePlanId(medicalCoverageId, planId);
    }

    @Override
    public boolean deleteByMedicalCoverageIdAndCoveragePlanId(String medicalCoverageId, String coverageId) {
        associatedCoverageVaccinationRepository.deleteByMedicalCoverageIdAndCoveragePlanId(medicalCoverageId, coverageId);
        return true;
    }

    @Override
    public AssociatedCoverageVaccination save(AssociatedCoverageVaccination associatedCoverageVaccination) {
        return associatedCoverageVaccinationRepository.save(associatedCoverageVaccination);
    }
}
