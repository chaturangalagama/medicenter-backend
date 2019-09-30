package com.ilt.cms.db.service.patient;

import com.ilt.cms.core.entity.patient.MedicalAlert;
import com.ilt.cms.database.patient.MedicalAlertDatabaseService;
import com.ilt.cms.repository.patient.MedicalAlertRepository;
import org.springframework.stereotype.Service;

@Service
public class MongoMedicalAlertDatabaseService implements MedicalAlertDatabaseService {

    public MedicalAlertRepository medicalAlertRepository;

    public MongoMedicalAlertDatabaseService(MedicalAlertRepository medicalAlertRepository){
        this.medicalAlertRepository = medicalAlertRepository;
    }

    @Override
    public MedicalAlert findByPatientId(String patientId) {
        return medicalAlertRepository.findByPatientId(patientId);
    }

    @Override
    public MedicalAlert save(MedicalAlert medicalAlert) {
        return medicalAlertRepository.save(medicalAlert);
    }

    @Override
    public MedicalAlert findByMedicalAlertId(String medicalAlertId) {
        return medicalAlertRepository.findByMedicalAlertId(medicalAlertId);
    }
}
