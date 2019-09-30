package com.ilt.cms.repository.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultationFollowupRepository extends MongoRepository<ConsultationFollowup, String> {

    List<ConsultationFollowup> findByFollowupDateBetweenAndReminderStatusReminderSent(LocalDate start, LocalDate end,
                                                                                      boolean reminderSent, Pageable pageable);

    List<ConsultationFollowup> findByFollowupDateBetweenAndReminderStatusIsNull(LocalDate start, LocalDate end, Pageable pageable);

    List<ConsultationFollowup> findByClinicIdAndFollowupDateBetween(String clinicId, LocalDate start, LocalDate end, Sort sort);

}
