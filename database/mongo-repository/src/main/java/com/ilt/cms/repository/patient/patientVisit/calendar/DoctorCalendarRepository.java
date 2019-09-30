package com.ilt.cms.repository.patient.patientVisit.calendar;

import com.ilt.cms.core.entity.calendar.DoctorCalendar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorCalendarRepository extends MongoRepository<DoctorCalendar, String> {

    List<DoctorCalendar> findByClinicId(String clinicId);

    List<DoctorCalendar> findByDoctorId(String doctorId);

    DoctorCalendar findByDoctorIdAndClinicId(String doctorId, String clinicId);

}
