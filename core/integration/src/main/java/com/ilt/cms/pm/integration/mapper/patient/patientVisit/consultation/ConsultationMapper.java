package com.ilt.cms.pm.integration.mapper.patient.patientVisit.consultation;

import com.ilt.cms.api.entity.consultation.ConsultationEntity;
import com.ilt.cms.core.entity.consultation.Consultation;

public class ConsultationMapper {

    public static ConsultationEntity mapToEntity(Consultation consultation) {
        if (consultation == null) {
            return null;
        }

        ConsultationEntity consultationEntity = new ConsultationEntity();
        consultationEntity.setConsultationId(consultation.getId());
        consultationEntity.setPatientId(consultation.getPatientId());
        consultationEntity.setClinicId(consultation.getClinicId());
        consultationEntity.setDoctorId(consultation.getDoctorId());
        consultationEntity.setClinicNotes(consultation.getClinicNotes());
        consultationEntity.setConsultationEndTime(consultation.getConsultationEndTime());
        consultationEntity.setConsultationStartTime(consultation.getConsultationStartTime());
        consultationEntity.setConsultationNotes(consultation.getConsultationNotes());
        consultationEntity.setMemo(consultation.getMemo());
        return consultationEntity;
    }

    public static Consultation mapToCore(ConsultationEntity consultationEntity) {
        if (consultationEntity == null) {
            return null;
        }
        Consultation consultation = new Consultation();
        consultation.setId(consultationEntity.getConsultationId());
        consultation.setPatientId(consultationEntity.getPatientId());
        consultation.setClinicId(consultationEntity.getClinicId());
        consultation.setDoctorId(consultationEntity.getDoctorId());
        consultation.setClinicNotes(consultationEntity.getClinicNotes());
        consultation.setConsultationNotes(consultationEntity.getConsultationNotes());
        consultation.setMemo(consultationEntity.getMemo());
        return consultation;
    }
}
