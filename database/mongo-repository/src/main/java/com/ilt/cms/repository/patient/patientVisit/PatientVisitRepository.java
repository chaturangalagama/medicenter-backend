package com.ilt.cms.repository.patient.patientVisit;

import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientVisitRepository extends MongoRepository<PatientVisitRegistry, String> {

    List<PatientVisitRegistry> findAllByClinicIdAndStartTimeBetween(String clinicId, LocalDateTime start, LocalDateTime end, Sort sort);

    Optional<PatientVisitRegistry> findFirstByClinicIdAndStartTimeBetweenAndPatientQueuePatientCalled(String clinicId,
                                                                                                  LocalDateTime startTime,
                                                                                                  LocalDateTime startTime2,
                                                                                                  boolean patientCalled, Sort sort);

    Page<PatientVisitRegistry> findAllByClinicIdInAndStartTimeBetween(List<String> clinicIds, LocalDateTime start,
                                                                      LocalDateTime end, Pageable pageable);

//    @Query(value = "{$and:[{clinicId:{$in:?0}}, {startTime:{$gt: ?1}}, {endTime:{$lt:?2}}]}")
    List<PatientVisitRegistry> findAllByClinicIdInAndStartTimeGreaterThanAndEndTimeLessThan(List<String> clinicIds,
                                                                                            LocalDateTime start,
                                                                                            LocalDateTime end);

    Page<PatientVisitRegistry> findAllByPatientIdAndStartTimeBetween(String patientId, LocalDateTime start, LocalDateTime end,
                                                                     Pageable pageable);

    List<PatientVisitRegistry> findAllByPatientIdAndStartTimeBetween(String patientId, LocalDateTime start, LocalDateTime end);

    List<PatientVisitRegistry> findAllByPatientIdAndStartTimeBetween(String patientId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<PatientVisitRegistry> findAllByPatientId(String patientId, Pageable pageable);

    List<PatientVisitRegistry> findAllByPatientId(String patientId);

    List<PatientVisitRegistry> findAllByStartTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Optional<PatientVisitRegistry> findByVisitNumber(String visitNumber);

    boolean existsByVisitNumber(String visitNumber);

    List<PatientVisitRegistry> findALLByPreferredDoctorIdAndStartTimeBetween(String preferredDoctorId, LocalDateTime start, LocalDateTime end);

    List<PatientVisitRegistry> findALLByPreferredDoctorIdAndClinicIdAndStartTimeBetween(String preferredDoctorId, String clinicId, LocalDateTime start, LocalDateTime end);
}
