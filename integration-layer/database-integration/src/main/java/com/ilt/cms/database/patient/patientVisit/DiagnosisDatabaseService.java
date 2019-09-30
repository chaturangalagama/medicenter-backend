package com.ilt.cms.database.patient.patientVisit;

import com.ilt.cms.core.entity.diagnosis.Diagnosis;

import java.util.List;

public interface DiagnosisDatabaseService {
    List<Diagnosis> findAll(List<String> diagnosisIds);

    List<Diagnosis> search(String term);

    boolean exists(String diagnosisId);
}