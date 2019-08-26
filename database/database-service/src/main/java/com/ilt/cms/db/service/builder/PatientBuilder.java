package com.ilt.cms.db.service.builder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.repository.CustomMedicalCoverageRepository;
import com.ilt.cms.repository.PolicyHolderRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderChasRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderCorporateRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderInsuranceRepository;
import com.ilt.cms.repository.spring.policy.PolicyHolderMediSaveRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PatientBuilder {

    private final CustomMedicalCoverageRepository coverageRepository;
    private PolicyHolderChasRepository chasRepository;
    private PolicyHolderCorporateRepository corporateRepository;
    private PolicyHolderInsuranceRepository insuranceRepository;
    private PolicyHolderMediSaveRepository mediSaveRepository;


    public PatientBuilder(CustomMedicalCoverageRepository coverageRepository,
                          PolicyHolderRepository holderRepository) {

        this.chasRepository = holderRepository.getChasRepository();
        this.corporateRepository = holderRepository.getCorporateRepository();
        this.insuranceRepository = holderRepository.getInsuranceRepository();
        this.mediSaveRepository = holderRepository.getMediSaveRepository();
        this.coverageRepository = coverageRepository;
    }


    public Patient build(Patient patient) {
        return patient;
    }

    /**
     * Populates the coverage details for the patient
     *
     * @param patient returns the same patient object with the coverage details populated
     * @return
     */
    public Patient buildCoverages(Patient patient) {

        List<PolicyHolder.PolicyHolderChas> chas = chasRepository.findByIdentificationNumber(patient.getUserId());
        List<PolicyHolder.PolicyHolderCorporate> corporate = corporateRepository.findAllByIdentificationNumber(patient.getUserId());
        List<PolicyHolder.PolicyHolderInsurance> insurance = insuranceRepository.findAllByIdentificationNumberAndStatus(patient.getUserId(), Status.ACTIVE);
        List<PolicyHolder.PolicyHolderMediSave> mediSave = mediSaveRepository.findByIdentificationNumber(patient.getUserId());

        if (chas != null) {
            populatePatient(patient, chas);
        }
        if (corporate != null) {
                populatePatient(patient, corporate);
        }
        if (mediSave != null) {
            populatePatient(patient, mediSave);
        }

        populatePatient(patient, insurance);

        return patient;
    }

    public Patient buildVaccination(Patient patient) {
        throw new UnsupportedOperationException("Method not completed yet");

    }

    private void populatePatient(Patient patient, List<? extends PolicyHolder> policyHolderList) {
        for(PolicyHolder holder : policyHolderList) {
            Optional<MedicalCoverage> optMedicalCoverage = coverageRepository.getMedicalCoverageRepository()
                    .findById(holder.getMedicalCoverageId());
            if (optMedicalCoverage.isPresent()) {

                MedicalCoverage medicalCoverage = optMedicalCoverage.get();
                CoveragePlan cp = findPlan(holder, medicalCoverage);

                patient.supplyCoverageDetails(medicalCoverage, cp);
            }
        }
    }

    //don't use streams here it will slow down the transaction, if you think stream is faster than this, speak to me me : name Jason.
    private CoveragePlan findPlan(PolicyHolder policyHolder, MedicalCoverage medicalCoverage) {
        for (CoveragePlan coveragePlan : medicalCoverage.getCoveragePlans()) {
            if (coveragePlan.getId().equals(policyHolder.getPlanId())) {
                return coveragePlan;
            }
        }
        return null;
    }
}
