package com.ilt.cms.pm.integration.mapper.patient.patientVisit.consultation;

import com.ilt.cms.api.entity.patientVisitRegistry.ConsultationFollowUp;
import com.ilt.cms.api.entity.reminder.ReminderStatusEntity;
import com.ilt.cms.core.entity.reminder.ReminderStatus;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;

public class ConsultationFollowUpMapper {

    public static ConsultationFollowUp mapToEntity(ConsultationFollowup consultationFollowup) {
        if (consultationFollowup == null) {
            return null;
        }
        ConsultationFollowUp followUp = new ConsultationFollowUp();
        followUp.setFollowupId(consultationFollowup.getId());
        followUp.setClinicId(consultationFollowup.getClinicId());
        followUp.setDoctorId(consultationFollowup.getDoctorId());
        followUp.setFollowupDate(consultationFollowup.getFollowupDate());
        followUp.setPatientId(consultationFollowup.getPatientId());
        followUp.setPatientVisitId(consultationFollowup.getPatientVisitId());
        followUp.setRemarks(consultationFollowup.getRemarks());
        if (consultationFollowup.getReminderStatus() != null) {
            followUp.setReminderStatus(mapReminderStatusToEntity(consultationFollowup.getReminderStatus()));
        }
        return followUp;
    }

    public static ConsultationFollowup mapToCore(ConsultationFollowUp consultationFollowUp) {
        if (consultationFollowUp == null) {
            return null;
        }
        ConsultationFollowup followup = new ConsultationFollowup();
        followup.setId(consultationFollowUp.getFollowupId());
        followup.setClinicId(consultationFollowUp.getClinicId());
        followup.setDoctorId(consultationFollowUp.getDoctorId());
        followup.setFollowupDate(consultationFollowUp.getFollowupDate());
        followup.setPatientId(consultationFollowUp.getPatientId());
        followup.setPatientVisitId(consultationFollowUp.getPatientVisitId());
        followup.setRemarks(consultationFollowUp.getRemarks());
        if (consultationFollowUp.getReminderStatus() != null) {
            followup.setReminderStatus(mapReminderStatusEntityToCore(consultationFollowUp.getReminderStatus()));
        }
        return followup;
    }

    private static ReminderStatusEntity mapReminderStatusToEntity(ReminderStatus reminderStatus) {
        if (reminderStatus == null) {
            return null;
        }
        ReminderStatusEntity entity = new ReminderStatusEntity();
        entity.setExternalReferenceNumber(reminderStatus.getExternalReferenceNumber());
        entity.setRemark(reminderStatus.getRemark());
        entity.setReminderSentTime(reminderStatus.getReminderSentTime());
        entity.setReminderSent(reminderStatus.isReminderSent());
        entity.setSentSuccessfully(reminderStatus.isSentSuccessfully());
        return entity;
    }

    private static ReminderStatus mapReminderStatusEntityToCore(ReminderStatusEntity reminderStatusEntity) {
        if (reminderStatusEntity == null) {
            return null;
        }
        ReminderStatus reminder = new ReminderStatus();
        reminder.setExternalReferenceNumber(reminderStatusEntity.getExternalReferenceNumber());
        reminder.setRemark(reminderStatusEntity.getRemark());
        reminder.setReminderSentTime(reminderStatusEntity.getReminderSentTime());
        reminder.setReminderSent(reminderStatusEntity.isReminderSent());
        reminder.setSentSuccessfully(reminderStatusEntity.isSentSuccessfully());
        return reminder;
    }

}
