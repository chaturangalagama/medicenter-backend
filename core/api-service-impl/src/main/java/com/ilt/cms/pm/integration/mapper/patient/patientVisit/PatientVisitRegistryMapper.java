package com.ilt.cms.pm.integration.mapper.patient.patientVisit;

import com.ilt.cms.api.entity.patientVisitRegistry.VisitRegistryEntity;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.core.entity.visit.Priority;

public class PatientVisitRegistryMapper {

    public static VisitRegistryEntity mapToEntity(PatientVisitRegistry patientVisitRegistry) {
        if (patientVisitRegistry == null) {
            return null;
        }
        VisitRegistryEntity registryEntity = new VisitRegistryEntity();
        registryEntity.setVisitId(patientVisitRegistry.getId());
        registryEntity.setVisitNumber(patientVisitRegistry.getVisitNumber());
        registryEntity.setClinicId(patientVisitRegistry.getClinicId());
        registryEntity.setPatientId(patientVisitRegistry.getPatientId());
        registryEntity.setStartTime(patientVisitRegistry.getStartTime());
        registryEntity.setEndTime(patientVisitRegistry.getEndTime());
        registryEntity.setPreferredDoctorId(patientVisitRegistry.getPreferredDoctorId());
        registryEntity.setVisitPurpose(patientVisitRegistry.getVisitPurpose());
        if (patientVisitRegistry.getVisitStatus() != null) {
            registryEntity.setVisitStatus(VisitRegistryEntity.PatientVisitState.valueOf(patientVisitRegistry.getVisitStatus().name()));
        }
        if (patientVisitRegistry.getPriority() != null) {
            registryEntity.setPriority(VisitRegistryEntity.Priority.valueOf(patientVisitRegistry.getPriority().name()));
        }
        if (patientVisitRegistry.getMedicalReference() != null) {
            registryEntity.setPatientReferralEntity(PatientReferralMapper.mapToEntity(patientVisitRegistry.getMedicalReference()));
        }
        registryEntity.setPatientQueue(patientVisitRegistry.getPatientQueue());
        registryEntity.setRemark(patientVisitRegistry.getRemark());
        return registryEntity;
    }

    public static PatientVisitRegistry mapToCore(VisitRegistryEntity registryEntity) {
        PatientVisitRegistry visitRegistry = new PatientVisitRegistry();
        visitRegistry.setVisitNumber(registryEntity.getVisitNumber());
        visitRegistry.setClinicId(registryEntity.getClinicId());
        visitRegistry.setPatientId(registryEntity.getPatientId());
        visitRegistry.setStartTime(registryEntity.getStartTime());
        visitRegistry.setEndTime(registryEntity.getEndTime());
        visitRegistry.setPreferredDoctorId(registryEntity.getPreferredDoctorId());
        visitRegistry.setVisitPurpose(registryEntity.getVisitPurpose());
        if (registryEntity.getVisitStatus() != null) {
            visitRegistry.setVisitStatus(PatientVisitRegistry.PatientVisitState.valueOf(registryEntity.getVisitStatus().name()));
        }
        if (registryEntity.getPriority() != null) {
            visitRegistry.setPriority(Priority.valueOf(registryEntity.getPriority().name()));
        }
        if (registryEntity.getPatientReferralEntity() != null) {
            visitRegistry.setMedicalReference(PatientReferralMapper.mapToCore(registryEntity.getPatientReferralEntity()));
        }
        visitRegistry.setRemark(registryEntity.getRemark());
        return visitRegistry;
    }

}
