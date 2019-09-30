package com.ilt.cms.repository.patient.patientVisit.calendar;

import com.ilt.cms.core.entity.calendar.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    List<Appointment> findByClinicIdAndDoctorIdAndStartDateBetween(String clinicId, String doctorId,
                                                                   LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findByClinicIdAndStartDateBetween(String clinicId,
                                                        LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findByClinicIdInOrDoctorIdInAndStartDateBetween(List<String> clinicIds, List<String> doctorIds,
                                                                      LocalDateTime startDate, LocalDateTime endDate);
}
