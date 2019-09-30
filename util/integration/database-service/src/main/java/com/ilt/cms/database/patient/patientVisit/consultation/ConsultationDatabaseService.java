package com.ilt.cms.database.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.consultation.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationDatabaseService {
    List<Consultation> findByPatientId(String patientId, Pageable pageable);

    List<Consultation> findByPatientId(String patientId);

    List<Consultation> findByPatientIdAndConsultationStartTimeBetween(String patientId, LocalDateTime start, LocalDateTime end);

    Page<Consultation> findAllByDoctorIdInAndConsultationStartTimeBetween(List<String> doctorIds, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Consultation> findFollowUpItems(LocalDate date);

    Consultation searchById(String consultationId);

    Consultation save(Consultation consultation);

    boolean exists(String consultationId);
}
