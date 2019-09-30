package com.ilt.cms.pm.integration.mapper.patient.patientVisit;

import com.ilt.cms.api.entity.patientVisitRegistry.VisitPurposeEntity;
import com.ilt.cms.core.entity.visit.VisitPurpose;

import java.util.function.Function;

public class VisitPurposeMapper {

    public static VisitPurpose mapToCore(VisitPurposeEntity visitPurposeEntity){
        if(visitPurposeEntity == null){
            return null;
        }
        VisitPurpose visitPurpose = new VisitPurpose();
        visitPurpose.setId(visitPurposeEntity.getId());
        visitPurpose.setName(visitPurposeEntity.getName());
        visitPurpose.setConsultationRequired(visitPurposeEntity.isConsultationRequired());

        return visitPurpose;
    }

    public static VisitPurposeEntity mapToEntity(VisitPurpose visitPurpose){
        if(visitPurpose == null){
            return null;
        }
        VisitPurposeEntity visitPurposeEntity = new VisitPurposeEntity();
        visitPurposeEntity.setId(visitPurpose.getId());
        visitPurposeEntity.setName(visitPurpose.getName());
        visitPurposeEntity.setConsultationRequired(visitPurpose.isConsultationRequired());
        return visitPurposeEntity;
    }
}
