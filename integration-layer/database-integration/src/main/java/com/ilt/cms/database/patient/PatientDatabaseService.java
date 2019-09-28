package com.ilt.cms.database.patient;

import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.patient.Patient;
import com.lippo.cms.exception.PatientException;
import com.lippo.commons.util.exception.RestValidationException;

import java.util.List;
import java.util.Optional;

public interface PatientDatabaseService {

    Patient save(Patient patient);

    boolean exists(String patientId);

    Patient patientRegistration(Patient patient) throws RestValidationException, PatientException;

    Optional<Patient> findPatientById(String patientId);

    Patient findPatient(String type, String searchId) throws PatientException ;

    boolean validateIdNumberUse(String searchId) throws PatientException ;

    Patient updatePatient(String patientId, Patient patientUpdate) throws PatientException ;

    List<Patient> likeSearchPatient(String value);

    Patient likeSearchPatient(String patientId, String nameOrNirc);

    boolean isUserIdAvailable(UserId userId);

    List<Patient> findAll(List<String> patientIds);
}
