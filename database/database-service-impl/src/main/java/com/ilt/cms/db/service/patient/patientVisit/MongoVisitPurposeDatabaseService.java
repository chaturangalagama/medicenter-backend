package com.ilt.cms.db.service.patient.patientVisit;

import com.ilt.cms.core.entity.visit.VisitPurpose;
import com.ilt.cms.database.patient.patientVisit.VisitPurposeDatabaseService;
import com.ilt.cms.repository.patient.patientVisit.VisitPurposeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoVisitPurposeDatabaseService implements VisitPurposeDatabaseService {

    private VisitPurposeRepository visitPurposeRepository;

    public MongoVisitPurposeDatabaseService(VisitPurposeRepository visitPurposeRepository){
        this.visitPurposeRepository = visitPurposeRepository;
    }
    @Override
    public boolean checkIfNameExists(String name) {
        return visitPurposeRepository.checkIfNameExists(name);
    }

    @Override
    public boolean checkIfNameExistsNotTheSameId(String name, String currentId) {
        return visitPurposeRepository.checkIfNameExistsNotTheSameId(name, currentId);
    }

    @Override
    public List<VisitPurpose> findAll() {
        return visitPurposeRepository.findAll();
    }

    @Override
    public VisitPurpose save(VisitPurpose visitPurpose) {
        return visitPurposeRepository.save(visitPurpose);
    }

    @Override
    public VisitPurpose findOne(String visitPurposeId) {
        return visitPurposeRepository.findById(visitPurposeId).orElse(null);
    }

    @Override
    public boolean delete(String visitPurposeId) {
        visitPurposeRepository.deleteById(visitPurposeId);
        return true;
    }
}
