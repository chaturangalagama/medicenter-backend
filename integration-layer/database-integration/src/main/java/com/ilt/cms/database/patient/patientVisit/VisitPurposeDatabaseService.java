package com.ilt.cms.database.patient.patientVisit;

import com.ilt.cms.core.entity.visit.VisitPurpose;

import java.util.List;

public interface VisitPurposeDatabaseService {
    boolean checkIfNameExists(String name);

    boolean checkIfNameExistsNotTheSameId(String name, String currentId);

    List<VisitPurpose> findAll();

    VisitPurpose save(VisitPurpose visitPurpose);

    VisitPurpose findOne(String visitPurposeId);

    boolean delete(String visitPurposeId);
}
