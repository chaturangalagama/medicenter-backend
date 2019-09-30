package com.ilt.cms.pm.integration.mapper.patient.patientVisit;

import com.ilt.cms.api.entity.calendar.*;
import com.ilt.cms.core.entity.calendar.*;

import java.util.stream.Collectors;

public class AppointmentMapper {

    public static Appointment mapToCore(AppointmentEntity appointmentEntity){
        Appointment appointment = new Appointment();
        appointment.setId(appointmentEntity.getId());
        appointment.setPatientId(appointmentEntity.getPatientId());
        appointment.setClinicId(appointmentEntity.getClinicId());
        appointment.setAppointmentPurpose(appointmentEntity.getAppointmentPurpose());
        appointment.setRemark(appointmentEntity.getRemark());
        appointment.setReminderDate(appointmentEntity.getReminderDate());
        appointment.setStartDate(appointmentEntity.getStartDate());
        appointment.setDuration(appointmentEntity.getDuration());

        return appointment;
    }

    public static AppointmentEntity mapToEntity(Appointment appointment){
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(appointment.getId());
        appointmentEntity.setPatientId(appointment.getPatientId());
        appointmentEntity.setClinicId(appointment.getClinicId());
        appointmentEntity.setAppointmentPurpose(appointment.getAppointmentPurpose());
        appointmentEntity.setRemark(appointment.getRemark());
        appointmentEntity.setReminderDate(appointment.getReminderDate());
        appointmentEntity.setStartDate(appointment.getStartDate());
        appointmentEntity.setDuration(appointment.getDuration());

        return appointmentEntity;
    }

    public static CalendarTimeSlot mapToCore(CalendarTimeSlotEntity calendarTimeSlotEntity){
        if(calendarTimeSlotEntity == null){
            return null;
        }
        CalendarTimeSlot calendarTimeSlot = new CalendarTimeSlot();
        if(calendarTimeSlotEntity.getCalendarDayWeek() != null) {
            calendarTimeSlot = new CalendarTimeSlot(mapToCore(calendarTimeSlotEntity.getCalendarDayWeek()),
                    calendarTimeSlotEntity.getStart(), calendarTimeSlotEntity.getEnd());
        }else if(calendarTimeSlotEntity.getCalendarDayYear() != null){
            calendarTimeSlot = new CalendarTimeSlot(mapToCore(calendarTimeSlotEntity.getCalendarDayYear()),
                    calendarTimeSlotEntity.getStart(), calendarTimeSlotEntity.getEnd());
        }
        return calendarTimeSlot;
    }

    public static CalendarTimeSlotEntity mapToEntity(CalendarTimeSlot calendarTimeSlot){
        if(calendarTimeSlot == null){
            return null;
        }
        CalendarTimeSlotEntity calendarTimeSlotEntity = new CalendarTimeSlotEntity();
        if(calendarTimeSlot.getCalendarDay() instanceof CalendarDayOfWeek){
            calendarTimeSlotEntity = new CalendarTimeSlotEntity(mapToEntity((CalendarDayOfWeek)calendarTimeSlot.getCalendarDay()),
                    null, calendarTimeSlot.getStart(), calendarTimeSlot.getEnd());
        }else if(calendarTimeSlot.getCalendarDay() instanceof CalendarDayOfYear){
            calendarTimeSlotEntity = new CalendarTimeSlotEntity(null, mapToEntity((CalendarDayOfYear) calendarTimeSlot.getCalendarDay()),
                    calendarTimeSlot.getStart(), calendarTimeSlot.getEnd());
        }

        return calendarTimeSlotEntity;
    }

    public static CalendarDay mapToCore(CalendarDayOfYearEntity calendarDayOfYearEntity){
        if(calendarDayOfYearEntity == null){
            return null;
        }
        CalendarDayOfYear calendarDayOfYear = new CalendarDayOfYear(calendarDayOfYearEntity.getDate());
        return calendarDayOfYear;
    }

    public static CalendarDayOfYearEntity mapToEntity(CalendarDayOfYear calendarDayOfYear){
        if(calendarDayOfYear == null){
            return null;
        }
        CalendarDayOfYearEntity calendarDayOfYearEntity = new CalendarDayOfYearEntity(calendarDayOfYear.getDate());
        return calendarDayOfYearEntity;
    }

    public static CalendarDay mapToCore(CalendarDayOfWeekEntity calendarDayOfWeekEntity){
        if(calendarDayOfWeekEntity == null){
            return null;
        }
        CalendarDayOfWeek calendarDayOfWeek = new CalendarDayOfWeek(calendarDayOfWeekEntity.getDayOfWeek());
        return calendarDayOfWeek;
    }

    public static CalendarDayOfWeekEntity mapToEntity(CalendarDayOfWeek calendarDayOfWeek){
        if(calendarDayOfWeek == null){
            return null;
        }
        CalendarDayOfWeekEntity calendarDayOfWeekEntity = new CalendarDayOfWeekEntity(calendarDayOfWeek.getDayOfWeek());
        return calendarDayOfWeekEntity;
    }

    public static ClinicCalendar mapToCore(ClinicCalendarEntity clinicCalendarEntity){
        if(clinicCalendarEntity == null){
            return null;
        }
        ClinicCalendar clinicCalendar = new ClinicCalendar();
        clinicCalendar.setId(clinicCalendarEntity.getId());
        clinicCalendar.setClinicId(clinicCalendarEntity.getClinicId());
        clinicCalendar.setAvgConsultationTime(clinicCalendarEntity.getAvgConsultationTime());
        clinicCalendar.setMaxWaitTime(clinicCalendarEntity.getMaxWaitTime());
        if(clinicCalendarEntity.getClinicHolidays() != null) {
            clinicCalendar.setClinicHolidays(clinicCalendarEntity.getClinicHolidays().stream()
                    .map(calendarTimeSlotEntity -> mapToCore(calendarTimeSlotEntity))
                    .collect(Collectors.toList()));
        }
        if(clinicCalendarEntity.getOperationHours() != null){
            clinicCalendar.setOperationHours(clinicCalendarEntity.getOperationHours().stream()
                    .map(calendarTimeSlotEntity -> mapToCore(calendarTimeSlotEntity))
                    .collect(Collectors.toList()));
        }
        return clinicCalendar;
    }

    public static ClinicCalendarEntity mapToEntity(ClinicCalendar clinicCalendar){
        if(clinicCalendar == null){
            return null;
        }
        ClinicCalendarEntity clinicCalendarEntity = new ClinicCalendarEntity();
        clinicCalendarEntity.setId(clinicCalendar.getId());
        clinicCalendarEntity.setClinicId(clinicCalendar.getClinicId());
        clinicCalendarEntity.setAvgConsultationTime(clinicCalendar.getAvgConsultationTime());
        clinicCalendarEntity.setMaxWaitTime(clinicCalendar.getMaxWaitTime());
        if(clinicCalendar.getClinicHolidays() != null) {
            clinicCalendarEntity.setClinicHolidays(clinicCalendar.getClinicHolidays().stream()
                    .map(calendarTimeSlot -> mapToEntity(calendarTimeSlot))
                    .collect(Collectors.toList()));
        }
        if(clinicCalendar.getOperationHours() != null){
            clinicCalendarEntity.setOperationHours(clinicCalendar.getOperationHours().stream()
                    .map(calendarTimeSlot -> mapToEntity(calendarTimeSlot))
                    .collect(Collectors.toList()));
        }
        return clinicCalendarEntity;
    }

    public static DoctorCalendar mapToCore(DoctorCalendarEntity doctorCalendarEntity){
        if(doctorCalendarEntity == null){
            return null;
        }
        DoctorCalendar doctorCalendar = new DoctorCalendar();
        doctorCalendar.setId(doctorCalendarEntity.getId());
        doctorCalendar.setClinicId(doctorCalendarEntity.getClinicId());
        doctorCalendar.setDoctorId(doctorCalendarEntity.getDoctorId());
        if(doctorCalendarEntity.getLeaves() != null) {
            doctorCalendar.setLeaves(doctorCalendarEntity.getLeaves().stream()
                    .map(calendarTimeSlotEntity -> mapToCore(calendarTimeSlotEntity))
                    .collect(Collectors.toList()));
        }
        if(doctorCalendarEntity.getWorkingDays() != null) {
            doctorCalendar.setWorkingDays(doctorCalendarEntity.getWorkingDays().stream()
                    .map(calendarTimeSlotEntity -> mapToCore(calendarTimeSlotEntity))
                    .collect(Collectors.toList()));
        }
        if(doctorCalendarEntity.getBlockedTime() != null) {
            doctorCalendar.setBlockedTime(doctorCalendarEntity.getBlockedTime().stream()
                    .map(calendarTimeSlotEntity -> mapToCore(calendarTimeSlotEntity))
                    .collect(Collectors.toList()));
        }

        return doctorCalendar;
    }

    public static DoctorCalendarEntity mapToEntity(DoctorCalendar doctorCalendar){
        if(doctorCalendar == null){
            return null;
        }
        DoctorCalendarEntity doctorCalendarEntity = new DoctorCalendarEntity();
        doctorCalendarEntity.setId(doctorCalendar.getId());
        doctorCalendarEntity.setClinicId(doctorCalendar.getClinicId());
        doctorCalendarEntity.setDoctorId(doctorCalendar.getDoctorId());
        if(doctorCalendar.getLeaves() != null) {
            doctorCalendarEntity.setLeaves(doctorCalendar.getLeaves().stream()
                    .map(calendarTimeSlot -> mapToEntity(calendarTimeSlot))
                    .collect(Collectors.toList()));
        }
        if(doctorCalendar.getWorkingDays() != null) {
            doctorCalendarEntity.setWorkingDays(doctorCalendar.getWorkingDays().stream()
                    .map(calendarTimeSlot -> mapToEntity(calendarTimeSlot))
                    .collect(Collectors.toList()));
        }
        if(doctorCalendar.getBlockedTime() != null) {
            doctorCalendarEntity.setBlockedTime(doctorCalendar.getBlockedTime().stream()
                    .map(calendarTimeSlot -> mapToEntity(calendarTimeSlot))
                    .collect(Collectors.toList()));
        }

        return doctorCalendarEntity;
    }

}
