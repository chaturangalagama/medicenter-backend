package com.ilt.cms.database.patient.patientVisit;

import com.ilt.cms.core.entity.calendar.Appointment;
import com.ilt.cms.core.entity.calendar.ClinicCalendar;
import com.ilt.cms.core.entity.calendar.DoctorCalendar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDatabaseService {


    ClinicCalendar findCalendar(String clinicId);

    List<DoctorCalendar> findDoctorCalendarByClinic(String clinicId);

    List<DoctorCalendar> findDoctorCalendarByDoctor(String doctorId);

    ClinicCalendar saveClinic(ClinicCalendar clinicCalendar);

    DoctorCalendar findDoctorCalendar(String clinicId, String doctorId);

    Appointment saveAppointment(Appointment appointment);

    List<Appointment> findAppointmentByClinicIdAndDoctorId(String clinicId, String doctorId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findAppointmentByClinicIdsOrDoctorIds(List<String> clinicIds, List<String> doctorIds, LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findAppointmentByClinicId(String clinicId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findAppointmentByDoctorId(String doctorId, LocalDateTime start, LocalDateTime end);

    DoctorCalendar saveDoctorCalendar(DoctorCalendar doctorCalendar);

    Optional<Appointment> findAppointmentById(String appointmentId);

    boolean removeDoctorCalendar(String appointmentId);
}
