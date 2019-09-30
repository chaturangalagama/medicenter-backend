package com.ilt.cms.db.service.patient.patientVisit;

import com.ilt.cms.core.entity.calendar.Appointment;
import com.ilt.cms.core.entity.calendar.ClinicCalendar;
import com.ilt.cms.core.entity.calendar.DoctorCalendar;
import com.ilt.cms.database.patient.patientVisit.AppointmentDatabaseService;

import com.ilt.cms.repository.patient.patientVisit.calendar.AppointmentRepository;
import com.ilt.cms.repository.patient.patientVisit.calendar.ClinicCalendarRepository;
import com.ilt.cms.repository.patient.patientVisit.calendar.DoctorCalendarRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MongoAppointmentDatabaseService implements AppointmentDatabaseService {

    private AppointmentRepository appointmentRepository;
    private ClinicCalendarRepository clinicCalendarRepository;
    private DoctorCalendarRepository doctorCalendarRepository;


    public MongoAppointmentDatabaseService(AppointmentRepository appointmentRepository, ClinicCalendarRepository clinicCalendarRepository, DoctorCalendarRepository doctorCalendarRepository) {
        this.appointmentRepository = appointmentRepository;
        this.clinicCalendarRepository = clinicCalendarRepository;
        this.doctorCalendarRepository = doctorCalendarRepository;
    }


    @Override
    public ClinicCalendar findCalendar(String clinicId) {
        return clinicCalendarRepository.findByClinicId(clinicId);
    }

    @Override
    public List<DoctorCalendar> findDoctorCalendarByClinic(String clinicId) {
        return doctorCalendarRepository.findByClinicId(clinicId);
    }

    @Override
    public List<DoctorCalendar> findDoctorCalendarByDoctor(String doctorId) {
        return doctorCalendarRepository.findByDoctorId(doctorId);
    }

    @Override
    public ClinicCalendar saveClinic(ClinicCalendar clinicCalendar) {
        return clinicCalendarRepository.save(clinicCalendar);
    }

    @Override
    public DoctorCalendar findDoctorCalendar(String clinicId, String doctorId) {
        return doctorCalendarRepository.findByDoctorIdAndClinicId(doctorId, clinicId);
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public Optional<Appointment> findAppointmentById(String appointmentId){
        return appointmentRepository.findById(appointmentId);
    }

    @Override
    public boolean removeDoctorCalendar(String appointmentId) {
        appointmentRepository.deleteById(appointmentId);
        return true;
    }

    @Override
    public List<Appointment> findAppointmentByClinicIdAndDoctorId(String clinicId, String doctorId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByClinicIdAndDoctorIdAndStartDateBetween(clinicId, doctorId, start, end);
    }

    @Override
    public List<Appointment> findAppointmentByClinicIdsOrDoctorIds(List<String> clinicIds, List<String> doctorIds, LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepository.findByClinicIdInOrDoctorIdInAndStartDateBetween(clinicIds, doctorIds, startDate, endDate);
    }

    @Override
    public List<Appointment> findAppointmentByClinicId(String clinicId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByClinicIdAndStartDateBetween(clinicId, start, end);
    }

    @Override
    public List<Appointment> findAppointmentByDoctorId(String doctorId, LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Override
    public DoctorCalendar saveDoctorCalendar(DoctorCalendar doctorCalendar) {
        return doctorCalendarRepository.save(doctorCalendar);
    }


}
