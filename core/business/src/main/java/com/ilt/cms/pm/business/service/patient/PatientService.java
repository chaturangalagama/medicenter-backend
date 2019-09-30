package com.ilt.cms.pm.business.service.patient;

import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.database.clinic.system.RunningNumberService;
import com.lippo.cms.exception.PatientException;
import com.lippo.commons.util.exception.RestValidationException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);
    private RunningNumberService runningNumberService;
    private PatientDatabaseService databaseService;


    public PatientService(RunningNumberService runningNumberService, PatientDatabaseService databaseService) {
        this.runningNumberService = runningNumberService;
        this.databaseService = databaseService;
    }

    public Patient save(Patient patient) {
        logger.info("Saving new patient [" + patient + "]");
        String patientNumber = runningNumberService.generatePatientNumber();
        logger.debug("generated new number [{}]", patientNumber);
        patient.setPatientNumber(patientNumber);
        return databaseService.save(patient);
    }

    public Patient patientRegistration(Patient patient) throws RestValidationException, PatientException {
        return databaseService.patientRegistration(patient);
    }

    public Patient findPatientById(String patientId) {
        Optional<Patient> patientOpt = databaseService.findPatientById(patientId);
        return patientOpt.orElse(null);
    }

    public Patient findPatient(String type, String searchId) throws PatientException {
        return databaseService.findPatient(type, searchId);
    }

    public boolean validateIdNumberUse(String searchId) throws PatientException {
        return databaseService.validateIdNumberUse(searchId);
    }

    public Patient updatePatient(String patientId, Patient patientUpdate) throws PatientException {
        return databaseService.updatePatient(patientId, patientUpdate);
    }

    public List<Patient> likeSearchPatient(String value) {
        return databaseService.likeSearchPatient(value);
    }

    public List<Patient> findAll(List<String> patientIds) {
        return databaseService.findAll(patientIds);
    }
}
