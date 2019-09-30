package com.ilt.cms.db.service.patient.patientVisit;

import com.ilt.cms.core.entity.diagnosis.Diagnosis;
import com.ilt.cms.database.patient.patientVisit.DiagnosisDatabaseService;
import com.ilt.cms.repository.patient.patientVisit.DiagnosisRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MongoDiagnosisDatabaseService implements DiagnosisDatabaseService {

    private DiagnosisRepository diagnosisRepository;
    public MongoDiagnosisDatabaseService(DiagnosisRepository diagnosisRepository){
        this.diagnosisRepository = diagnosisRepository;
    }
    @Override
    public List<Diagnosis> findAll(List<String> diagnosisIds) {
        List<Diagnosis> diagnoses = new ArrayList<>();
        diagnosisRepository.findAllById(diagnosisIds).forEach(diagnoses::add);
        return diagnoses;
    }

    @Override
    public List<Diagnosis> search(String term) {
        return diagnosisRepository.search(term);
    }

    @Override
    public boolean exists(String diagnosisId) {
        return diagnosisRepository.existsById(diagnosisId);
    }

}
