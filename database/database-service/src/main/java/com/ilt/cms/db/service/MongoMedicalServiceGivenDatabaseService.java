package com.ilt.cms.db.service;

import com.ilt.cms.database.medicalService.MedicalServiceGivenDatabaseService;
import com.ilt.cms.repository.spring.MedicalServiceGivenRepository;
import org.springframework.stereotype.Service;

@Service
public class MongoMedicalServiceGivenDatabaseService implements MedicalServiceGivenDatabaseService {

    private MedicalServiceGivenRepository medicalServiceGivenRepository;

    public MongoMedicalServiceGivenDatabaseService(MedicalServiceGivenRepository medicalServiceGivenRepository) {
        this.medicalServiceGivenRepository = medicalServiceGivenRepository;
    }

    @Override
    public boolean exists(String medicalServiceGivenId) {
        return medicalServiceGivenRepository.existsById(medicalServiceGivenId);
    }
}
