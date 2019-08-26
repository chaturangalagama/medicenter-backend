package com.ilt.cms.pm.business.service.claim;

import com.ilt.cms.repository.spring.*;
import com.ilt.cms.repository.spring.consultation.ConsultationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ClaimPopulateLogic {

    private static final Logger logger = LogManager.getLogger(ClaimPopulateLogic.class);

    private static final String GROUP_DR_ID = "main-group-doctor-id";

    private ConsultationRepository consultationRepository;

    private ClinicRepository clinicRepository;

    private DoctorRepository doctorRepository;

    private MedicalServiceRepository medicalServiceRepository;

    private PatientRepository patientRepository;

    private DiagnosisRepository diagnosisRepository;

    private CaseRepository caseRepository;

    private PatientVisitRegistryRepository patientVisitRegistryRepository;

    public ClaimPopulateLogic(ConsultationRepository consultationRepository,
                              ClinicRepository clinicRepository,
                              DoctorRepository doctorRepository,
                              MedicalServiceRepository medicalServiceRepository, PatientRepository patientRepository,
                              PatientVisitRegistryRepository patientVisitRegistryRepository,
                              DiagnosisRepository diagnosisRepository) {
        this.consultationRepository = consultationRepository;
        this.clinicRepository = clinicRepository;
        this.doctorRepository = doctorRepository;
        this.medicalServiceRepository = medicalServiceRepository;
        this.patientRepository = patientRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.patientVisitRegistryRepository = patientVisitRegistryRepository;
    }

/*
    public Optional<Claim> createClaim(String invoiceId, String patientVisitRegistryId) throws CMSException {

        logger.info("Creating a Claim for the invoice id [" + invoiceId + "]");
        List<Case> cases = caseRepository.findByInvoiceId(invoiceId);

        if (cases == null || cases.isEmpty()) {
            logger.info("No case found for the invoice id [" + invoiceId + "]");
            return Optional.empty();
        }

        Case caseForInvoice = cases.get(0);
        Optional<Invoice> invoiceForCase = caseRepository.findInvoiceById(invoiceId);

    }
*/



}
