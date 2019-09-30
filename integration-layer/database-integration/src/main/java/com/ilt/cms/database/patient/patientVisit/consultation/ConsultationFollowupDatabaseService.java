package com.ilt.cms.database.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

public interface ConsultationFollowupDatabaseService {
    //List<ConsultationFollowup> findByFollowupDateBetweenAndReminderStatusReminderSent(LocalDate start, LocalDate end, boolean reminderSent, Pageable pageable);

    //List<ConsultationFollowup> findByFollowupDateBetweenAndReminderStatusIsNull(LocalDate start, LocalDate end, Pageable pageable);

    //List<ConsultationFollowup> findByClinicIdAndFollowupDateBetween(String clinicId, LocalDate start, LocalDate end, Sort sort);

    ConsultationFollowup searchById(String followUpId);

    ConsultationFollowup save(ConsultationFollowup followup);

    boolean exists(String followUpId);

    List<ConsultationFollowup> findByClinicIdAndFollowupDateBetween(String clinicId, LocalDate startDate, LocalDate endDate, Sort followupDate);
}
