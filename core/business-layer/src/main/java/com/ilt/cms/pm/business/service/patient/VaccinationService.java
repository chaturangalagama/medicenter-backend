package com.ilt.cms.pm.business.service.patient;

import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.patient.PatientVaccination;
import com.ilt.cms.core.entity.vaccination.AssociatedCoverageVaccination;
import com.ilt.cms.core.entity.vaccination.Vaccination;
import com.ilt.cms.core.entity.vaccination.VaccinationScheme;
import com.ilt.cms.database.coverage.MedicalCoverageDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.database.vaccination.AssociatedCoverageVaccinationDatabaseService;
import com.ilt.cms.database.vaccination.VaccinationDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.CMSConstant;
import com.lippo.commons.util.CommonUtils;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@Service
public class VaccinationService {
    private static final Logger logger = LoggerFactory.getLogger(VaccinationService.class);
    private VaccinationDatabaseService vaccinationDatabaseService;
    private PatientDatabaseService patientDatabaseService;
    private AssociatedCoverageVaccinationDatabaseService associatedCoverageVaccinationDatabaseService;
    private MedicalCoverageDatabaseService medicalCoverageDatabaseService;

    public VaccinationService(VaccinationDatabaseService vaccinationDatabaseService,
                              PatientDatabaseService patientDatabaseService,
                              AssociatedCoverageVaccinationDatabaseService associatedCoverageVaccinationDatabaseService,
                              MedicalCoverageDatabaseService medicalCoverageDatabaseService) {
        this.vaccinationDatabaseService = vaccinationDatabaseService;
        this.patientDatabaseService = patientDatabaseService;
        this.associatedCoverageVaccinationDatabaseService = associatedCoverageVaccinationDatabaseService;
        this.medicalCoverageDatabaseService = medicalCoverageDatabaseService;
    }

    public HashMap<String, Object> listVaccines(int page, int size) {
        Page<Vaccination> vaccinations = vaccinationDatabaseService.findAll(new PageRequest(page, size));

        HashMap<String, Object> payload = new HashMap<>();
        payload.put(CMSConstant.PAYLOAD_KEY_CONTENT, vaccinations.getContent());
        payload.put(CMSConstant.PAYLOAD_KEY_TOTAL_ELEMENTS, vaccinations.getTotalElements());
        payload.put(CMSConstant.PAYLOAD_KEY_TOTAL_PAGES, vaccinations.getTotalPages());
        payload.put(CMSConstant.PAYLOAD_KEY_NUMBER, vaccinations.getNumber());
        return payload;
    }

    /**
     * Adds the vaccination to the given patient.
     *
     * @param patientId   : Patient to add the validation
     * @param vaccination : Vaccination information to be added. A patient can have multiples of the same vaccination.
     *                    This is not validated.
     * @return
     */
    public PatientVaccination addVaccinationToPatient(String patientId, PatientVaccination vaccination) throws CMSException {
        boolean parametersValid = vaccination.areParametersValid();
        if (!parametersValid) {
            //return new HttpApiResponse(StatusCode.E1006, "Vaccination parameters not valid");
            throw new CMSException(StatusCode.E1006, "Vaccination parameters not valid");
        } else {
            if (!vaccinationDatabaseService.isIdValid(vaccination.getVaccineId())) {
                //return new HttpApiResponse(StatusCode.E1006, "Given vaccine ID does not exists");
                throw new CMSException(StatusCode.E1006, "Given vaccine ID does not exists");
            } else {
                boolean scheduleMatches = vaccination.getVaccinationSchedules().stream()
                        .allMatch(vs -> vaccinationDatabaseService.isIdValid(vs.getVaccineId()));
                if (!scheduleMatches) {
                    //return new HttpApiResponse(StatusCode.E1006, "Some or all scheduled vaccine ID does not exists");
                    throw new CMSException(StatusCode.E1006, "Some or all scheduled vaccine ID does not exists");
                } else {
                    Optional<Patient> patientOpt = patientDatabaseService.findPatientById(patientId);
                    if (!patientOpt.isPresent()) {
                        //return new HttpApiResponse(StatusCode.E2000);
                        throw new CMSException(StatusCode.E2000);
                    } else {
                        Patient patient = patientOpt.get();
                        vaccination.setId(CommonUtils.idGenerator()); // set an ID
                        patient.addPatientVaccination(vaccination);
                        patientDatabaseService.save(patient);
                        return vaccination;
                    }
                }
            }
        }
    }

    public boolean removeVaccinationFromPatient(String patientId, String associationVaccinationId) throws CMSException {
        Optional<Patient> patientOpt = patientDatabaseService.findPatientById(patientId);
        if (!patientOpt.isPresent()) {
            //return new HttpApiResponse(StatusCode.E2000);
            throw new CMSException(StatusCode.E2000);
        } else {
            boolean removed = patientOpt.get().removePatientVaccination(associationVaccinationId);
            return removed;
            //return new HttpApiResponse(StatusCode.S0000, "Was there any removed [" + removed + "]");
        }
    }

    public Vaccination addVaccination(Vaccination vaccination) throws CMSException {

        if (!vaccination.areParametersValid()) {
            logger.error("Parameters for vaccine is not valid [" + vaccination + "]");
            //return new HttpApiResponse(StatusCode.E1002);
            throw new CMSException(StatusCode.E1002);
        } else {
            logger.debug("Vaccination info is correct, persisting info");
            Vaccination savedVersion = vaccinationDatabaseService.save(vaccination);
            return savedVersion;
        }
    }

    public List<Vaccination>  listAllVaccines() {
        List<Vaccination> vaccinations = vaccinationDatabaseService.findAll();
        return vaccinations;
    }

    public AssociatedCoverageVaccination vaccinationAssociation(AssociatedCoverageVaccination associatedCoverageVaccination, boolean update) throws CMSException {
        MedicalCoverage medicalCoverage = medicalCoverageDatabaseService.findOne(associatedCoverageVaccination.getMedicalCoverageId());

        if (medicalCoverage == null) {
            //return new CmsServiceResponse<>(StatusCode.E1005, "Medical Coverage not found");
            throw new CMSException(StatusCode.E1005, "Medical Coverage not found");
        } else {
            List<CoveragePlan> coveragePlans = medicalCoverage.getCoveragePlans();

            Optional<CoveragePlan> coveragePlanOptional = coveragePlans.stream()
                    .filter(plan -> plan.getId().equals(associatedCoverageVaccination.getCoveragePlanId()))
                    .findFirst();

            if (!coveragePlanOptional.isPresent()) {
                //return new CmsServiceResponse<>(StatusCode.E1005, "Plan not found");
                throw new CMSException(StatusCode.E1005, "Plan not found");
            } else {
                boolean valid = areMedicalTestSchemesValid(associatedCoverageVaccination.getModifiedVaccinationScheme(),
                        associatedCoverageVaccination.getExcludedVaccinationScheme());
                if (!valid) {
                    logger.warn("Medical Test ID specified not valid");
                    //return new CmsServiceResponse<>(StatusCode.E1005, "Some or all specified Vaccination dose ID not valid");
                    throw new CMSException(StatusCode.E1005, "Some or all specified Vaccination dose ID not valid");
                } else {
                    associatedCoverageVaccination.clearFieldsForPersistence();
                    if (update) {
                        logger.info("Update method called, dropping association [" + associatedCoverageVaccination.getMedicalCoverageId()
                                + "] before merging planId[" + associatedCoverageVaccination.getCoveragePlanId() + "]");
                        associatedCoverageVaccinationDatabaseService.deleteByMedicalCoverageIdAndCoveragePlanId(associatedCoverageVaccination.getMedicalCoverageId(),
                                associatedCoverageVaccination.getCoveragePlanId());
                    }
                    AssociatedCoverageVaccination persistedAssociatedCoverage = associatedCoverageVaccinationDatabaseService.save(associatedCoverageVaccination);
                    return persistedAssociatedCoverage;
                }
            }
        }
    }

    private boolean areMedicalTestSchemesValid(List<VaccinationScheme> modifiedMedicalTestScheme, List<VaccinationScheme> excludedMedicalTestScheme) {
        boolean valid = true;
        if (!modifiedMedicalTestScheme.isEmpty()) {
            valid = modifiedMedicalTestScheme.stream()
                    .allMatch(vaccinationScheme -> vaccinationDatabaseService.checkIfAllVaccinationDoseIdExists(vaccinationScheme.getDoseId()));
        }
        if (!excludedMedicalTestScheme.isEmpty()) {
            valid = valid
                    && excludedMedicalTestScheme.stream()
                    .allMatch(vaccinationScheme -> vaccinationDatabaseService.checkIfAllVaccinationDoseIdExists(vaccinationScheme.getDoseId()));
        }
        return valid;
    }

    public boolean removeAssociation(String medicalCoverageId, String associationCoverageId) {
        associatedCoverageVaccinationDatabaseService.deleteByIdAndMedicalCoverageId(associationCoverageId, medicalCoverageId);
        return true;
    }

    public Vaccination findFirstByDosesIn(String doseId){
        Vaccination vaccination = vaccinationDatabaseService.findFirstByDosesIn(doseId);
        return  vaccination;
    }
}
