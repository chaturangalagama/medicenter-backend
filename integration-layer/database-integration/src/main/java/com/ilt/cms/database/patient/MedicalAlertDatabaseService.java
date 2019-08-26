package com.ilt.cms.database.patient;

import com.ilt.cms.core.entity.patient.MedicalAlert;

public interface MedicalAlertDatabaseService {

    MedicalAlert findByPatientId(String patientId);

    MedicalAlert save(MedicalAlert medicalAlert);

    MedicalAlert findByMedicalAlertId(String medicalAlertId);
}
