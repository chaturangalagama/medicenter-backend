package com.ilt.cms.pm.integration.mapper.patient.patientVisit;

import com.ilt.cms.api.entity.diagnosis.DiagnosisEntity;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.diagnosis.Diagnosis;

public class DiagnosisMapper {
    public static Diagnosis mapToCore(DiagnosisEntity diagnosisEntity){
        if(diagnosisEntity == null){
            return null;
        }
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(diagnosisEntity.getId());
        diagnosis.setIcd10Code(diagnosisEntity.getIcd10Code());
        diagnosis.setIcd10Id(diagnosisEntity.getIcd10Id());
        diagnosis.setIcd10Term(diagnosisEntity.getIcd10Term());

        diagnosis.setSnomedCode(diagnosisEntity.getSnomedCode());
        diagnosis.setSnomedId(diagnosisEntity.getSnomedId());
        diagnosis.setSnomedTerm(diagnosisEntity.getSnomedTerm());
        if(diagnosisEntity.getStatus() != null) {
            diagnosis.setStatus(Status.valueOf(diagnosisEntity.getStatus().name()));
        }
        if(diagnosisEntity.getFilterablePlanIds() != null){
            diagnosis.setFilterablePlanIds(diagnosisEntity.getFilterablePlanIds());
        }

        return diagnosis;
    }

    public static DiagnosisEntity mapToEntity(Diagnosis diagnosis){
        if(diagnosis == null){
            return null;
        }
        DiagnosisEntity diagnosisEntity = new DiagnosisEntity();
        diagnosisEntity.setId(diagnosis.getId());
        diagnosisEntity.setIcd10Code(diagnosis.getIcd10Code());
        diagnosisEntity.setIcd10Id(diagnosis.getIcd10Id());
        diagnosisEntity.setIcd10Term(diagnosis.getIcd10Term());

        diagnosisEntity.setSnomedCode(diagnosis.getSnomedCode());
        diagnosisEntity.setSnomedId(diagnosis.getSnomedId());
        diagnosisEntity.setSnomedTerm(diagnosis.getSnomedTerm());
        if(diagnosis.getStatus() != null) {
            diagnosisEntity.setStatus(com.ilt.cms.api.entity.common.Status.valueOf(diagnosis.getStatus().name()));
        }
        if(diagnosis.getFilterablePlanIds() != null){
            diagnosisEntity.setFilterablePlanIds(diagnosis.getFilterablePlanIds());
        }

        return diagnosisEntity;
    }
}
