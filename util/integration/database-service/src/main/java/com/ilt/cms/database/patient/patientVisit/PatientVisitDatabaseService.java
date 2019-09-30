package com.ilt.cms.database.patient.patientVisit;

import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientVisitDatabaseService {

    PatientVisitRegistry save(PatientVisitRegistry destination);

    PatientVisitRegistry searchById(String id);

    List<PatientVisitRegistry> searchByIds(List<String> ids);

    PatientVisitRegistry searchByVisitNumber(String visitNumber);

    List<PatientVisitRegistry> listPatientVisits(String patientId);

    List<PatientVisitRegistry> listByClinicIdAndStartTime(String clinicId, LocalDateTime start, LocalDateTime end);

    void remove(String visitId);

    boolean exists(String visitId);

    Page<PatientVisitRegistry> listPatientVisits(String patientId, int page, int size, Sort sort);

    Page<PatientVisitRegistry> findByPatientIdAndStartTime(String patientId, LocalDateTime start, LocalDateTime end, int page, int size, Sort sort);

    List<PatientVisitRegistry> findByPatientIdAndStartTime(String patientId, LocalDateTime start, LocalDateTime end);

    List<PatientVisitRegistry> findByPreferredDoctorIdAndStartTime(String preferredDoctorId, LocalDateTime start, LocalDateTime end);

    List<PatientVisitRegistry> findByPreferredDoctorIdAndClinicIdAndStartTime(String preferredDoctorId, String clinicId, LocalDateTime start, LocalDateTime end);

    List<PatientVisitRegistry> findByClinicIdsAndStartEndTimeBetween(List<String> clinicIds, LocalDateTime start, LocalDateTime end);

    List<PatientVisitRegistry> searchClinicAndDoctorsByStartTime(List<String> clinicIds, List<String> doctorIds,
                                                                 LocalDateTime startTime, LocalDateTime endTime);
}
