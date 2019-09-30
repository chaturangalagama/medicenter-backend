package com.ilt.cms.repository.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.consultation.Consultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRepository extends MongoRepository<Consultation, String> {

    List<Consultation> findByPatientId(String patientId, Pageable pageable);

    List<Consultation> findByPatientId(String patientId);

    List<Consultation> findByPatientIdAndConsultationStartTimeBetween(String patientId, LocalDateTime start, LocalDateTime end);

    Page<Consultation> findAllByDoctorIdInAndConsultationStartTimeBetween(List<String> doctorIds, LocalDateTime start,
                                                                          LocalDateTime end, Pageable pageable);

    @Query(value = "{ 'medicalServiceItemList._id' : { $in : ?0} }")
    List<Consultation> findFollowUpItems(LocalDate date);
}
