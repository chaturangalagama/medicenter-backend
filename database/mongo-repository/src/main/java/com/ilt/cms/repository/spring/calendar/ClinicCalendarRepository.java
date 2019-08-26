package com.ilt.cms.repository.spring.calendar;

import com.ilt.cms.core.entity.calendar.Appointment;
import com.ilt.cms.core.entity.calendar.ClinicCalendar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicCalendarRepository extends MongoRepository<ClinicCalendar, String> {

    ClinicCalendar findByClinicId(String clinicId);
}
