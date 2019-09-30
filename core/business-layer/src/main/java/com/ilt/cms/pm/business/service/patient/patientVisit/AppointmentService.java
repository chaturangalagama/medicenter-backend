package com.ilt.cms.pm.business.service.patient.patientVisit;

import com.ilt.cms.core.entity.calendar.*;
import com.ilt.cms.database.patient.patientVisit.AppointmentDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private AppointmentDatabaseService appointmentDatabaseService;

    public AppointmentService(AppointmentDatabaseService appointmentDatabaseService) {
        this.appointmentDatabaseService = appointmentDatabaseService;
    }


    /*public ClinicCalendar clinicOperationHours(String clinicId) {
        logger.info("finding calendar for clinic [" + clinicId + "]");
        ClinicCalendar calendar = calendarDatabaseService.findCalendar(clinicId);
        logger.info("calendar details [" + calendar + "]");
        return calendar;
    }*/

    public List<Appointment> findClinicAppointments(String clinicId, LocalDateTime start, LocalDateTime end) {
        logger.info("finding all appointment for clinic[" + clinicId + "] start[" + start
                + "] end[" + end + "]");
        return appointmentDatabaseService.findAppointmentByClinicId(clinicId, start, end);
    }

    public List<Appointment> findClinicAppointments(String clinicId, String doctorId, LocalDateTime start,
                                                     LocalDateTime end) {
        logger.info("finding all appointment for clinic[" + clinicId + "] doctor[" + doctorId
                + "] start[" + start + "] end[" + end + "]");
        return appointmentDatabaseService.findAppointmentByClinicIdAndDoctorId(clinicId, doctorId, start, end);
    }

    public List<Appointment> findDoctorAppointments(String doctorId, LocalDateTime start,
                                                    LocalDateTime end) {
        logger.info("finding all appointment for doctor[" + doctorId
                + "] start[" + start + "] end[" + end + "]");
        return appointmentDatabaseService.findAppointmentByDoctorId(doctorId, start, end);
    }


    /*public ClinicCalendar updateClinicOperationHours(String clinicId, List<CalendarTimeSlot> operationHours) {
        ClinicCalendar clinicCalendar = calendarDatabaseService.findCalendar(clinicId);
        for (CalendarTimeSlot operationHour : operationHours) {
            if (operationHour.getStart() == null || operationHour.getEnd() == null) {
                logger.error("operating hours not available [" + operationHour + "]");
                throw new RuntimeException("Operating hours not set");
            } else if (operationHour.getCalendarDay() == null) {
                logger.error("operating day not set [" + operationHour + "]");
                throw new RuntimeException("Operating day not set");
            }
        }
        clinicCalendar.setOperationHours(operationHours);
        return calendarDatabaseService.saveClinic(clinicCalendar);
    }*/

    public DoctorCalendar addBlockDoctorTime(String clinicId, String doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        logger.info("Add blocking doctor [" + doctorId + "] for clinic [" + clinicId + "] timeslot[" + startTime + "][" + endTime + "]");

        DoctorCalendar doctorCalendar = appointmentDatabaseService.findDoctorCalendar(clinicId, doctorId);
        if (!(doctorCalendar.isDoctorAvailable(startTime)
                && doctorCalendar.isDoctorAvailable(endTime))) {
            logger.error("Doctor is not available to block this time slot");
            throw new RuntimeException("Doctor is not available to do this block");
        } else if (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            logger.error("Time block can be done only within a day, anything more than a day needs to be added as doctor leave");
            throw new RuntimeException("Time block can be done only within a day, anything more than a day needs to be added as doctor leave");
        }
        doctorCalendar.getBlockedTime().add(new CalendarTimeSlot(new CalendarDayOfYear(startTime.toLocalDate()),
                startTime.toLocalTime(), endTime.toLocalTime()));
        DoctorCalendar saveDoctorCalendar = appointmentDatabaseService.saveDoctorCalendar(doctorCalendar);
        return saveDoctorCalendar;
    }

    public boolean removeBlockDoctorTime(String clinicId, String doctorId, LocalDateTime startTime){
        logger.info("Remove blocking doctor [" + doctorId + "] for clinic [" + clinicId + "] timeslot[" + startTime + "]");
        DoctorCalendar doctorCalendar = appointmentDatabaseService.findDoctorCalendar(clinicId, doctorId);
        Optional<CalendarTimeSlot> calendarTimeSlotOpt = doctorCalendar.getBlockedTime().stream()
                .filter(calendarTimeSlot -> calendarTimeSlot.getCalendarDay().isDayMatches(startTime.toLocalDate())
                && calendarTimeSlot.getStart().isBefore(startTime.toLocalTime())
                && calendarTimeSlot.getEnd().isAfter(startTime.toLocalTime())).findFirst();

        boolean success = false;
        if(calendarTimeSlotOpt.isPresent()){
            logger.info("Block time slot is exist["+calendarTimeSlotOpt.get()+"]");
            CalendarTimeSlot blockTimeSlot = calendarTimeSlotOpt.get();
            success = doctorCalendar.getBlockedTime().remove(blockTimeSlot);
            appointmentDatabaseService.saveDoctorCalendar(doctorCalendar);
        }

        return success;
    }


    public Appointment createDoctorAppointment(Appointment appointment) throws CMSException {
        logger.info("Creating a new appointment [" + appointment + "]");
        if(appointment.getStartDate().isAfter(LocalDateTime.now().minusHours(1))){
            throw new CMSException(StatusCode.E1010, "Make appointment should before 1 hour");
        }

        ClinicCalendar clinicCalendar = appointmentDatabaseService.findCalendar(appointment.getClinicId());
        DoctorCalendar doctorCalendar = appointmentDatabaseService.findDoctorCalendar(appointment.getClinicId(),
                appointment.getDoctorId());

        boolean withinClinicHours = clinicCalendar.isWithinClinicHours(appointment.getStartDate());
        boolean doctorAvailable = true;
        if(doctorCalendar != null) {
            doctorAvailable = doctorCalendar.isDoctorAvailable(appointment.getStartDate());
        }

        boolean timeSlotAvailable = true;

        int maxClinicAppointmentPerHour = findMaxClinicAppointmentPerHour(clinicCalendar, appointment.getStartDate());

        List<Appointment> appointments = appointmentDatabaseService.findAppointmentByClinicId(appointment.getClinicId(),
                LocalDateTime.of(appointment.getStartDate().toLocalDate(), appointment.getStartDate().toLocalTime().withMinute(0)),
                LocalDateTime.of(appointment.getStartDate().toLocalDate(), appointment.getStartDate().toLocalTime().withMinute(59)));

        if(appointment.getDoctorId() != null) {
            int appointmentPerHour = findMaxDoctorAppointmentPerHour(clinicCalendar, appointment.getDoctorId(), appointment.getStartDate());
            List<Appointment> doctorAppointments = appointmentDatabaseService.findAppointmentByClinicIdAndDoctorId(appointment.getClinicId(), appointment.getDoctorId(),
                    LocalDateTime.of(appointment.getStartDate().toLocalDate(), appointment.getStartDate().toLocalTime().withMinute(0)),
                    LocalDateTime.of(appointment.getStartDate().toLocalDate(), appointment.getStartDate().toLocalTime().withMinute(59)));

            if (doctorAppointments.size() >= appointmentPerHour) {
                logger.info("Patient cannot create appointment due to doctor["+appointment.getDoctorId()+"] time slot is full[" + appointments.size() + "]["+appointmentPerHour+"]");
                timeSlotAvailable = false;
            }
            if(appointments.size() >= maxClinicAppointmentPerHour){
                logger.info("Patient cannot create appointment due to clinic time slot is full[" + appointments.size() + "]["+maxClinicAppointmentPerHour+"]");
                timeSlotAvailable = false;
            }
        }else{

            if(appointments.size() >= maxClinicAppointmentPerHour){
                logger.info("Patient cannot create appointment due to clinic time slot is full[" + appointments.size() + "]["+maxClinicAppointmentPerHour+"]");
                timeSlotAvailable = false;
            }
        }

        if (!(withinClinicHours && doctorAvailable && timeSlotAvailable)) {
            throw new RuntimeException("Appointment time is not valid clinicCalendar[" + clinicCalendar
                    + "] doctorCalendar[" + doctorAvailable + "]");
        } else {
            logger.info("saving appointment");
            return appointmentDatabaseService.saveAppointment(appointment);
        }
    }

    public Appointment modifyDoctorAppointment(String appointmentId, Appointment appointment) throws CMSException {
        logger.info("Modify a appointment [" + appointmentId + "] [" + appointment + "]");
        if(appointment.getStartDate().isAfter(LocalDateTime.now().minusHours(1))){
            throw new CMSException(StatusCode.E1010, "Make appointment should before 1 hour");
        }
        Optional<Appointment> appointmentOpt = appointmentDatabaseService.findAppointmentById(appointmentId);

        if(!appointmentOpt.isPresent()){
            throw new CMSException(StatusCode.E2000, "appointment not found");
        }
        Appointment existAppointment = appointmentOpt.get();

        existAppointment.setDoctorId(appointment.getDoctorId());
        existAppointment.setStartDate(appointment.getStartDate());
        existAppointment.setDuration(appointment.getDuration());
        existAppointment.setAppointmentPurpose(appointment.getAppointmentPurpose());
        existAppointment.setRemark(appointment.getRemark());

        ClinicCalendar clinicCalendar = appointmentDatabaseService.findCalendar(appointment.getClinicId());
        DoctorCalendar doctorCalendar = appointmentDatabaseService.findDoctorCalendar(appointment.getClinicId(),
                appointment.getDoctorId());

        boolean withinClinicHours = clinicCalendar.isWithinClinicHours(appointment.getStartDate());
        boolean doctorAvailable = true;
        if(doctorCalendar != null) {
            doctorAvailable = doctorCalendar.isDoctorAvailable(appointment.getStartDate());
        }

        boolean timeSlotAvailable = true;

        int maxClinicAppointmentPerHour = findMaxClinicAppointmentPerHour(clinicCalendar, appointment.getStartDate());

        List<Appointment> appointments = appointmentDatabaseService.findAppointmentByClinicId(appointment.getClinicId(),
                LocalDateTime.of(appointment.getStartDate().toLocalDate(), appointment.getStartDate().toLocalTime().withMinute(0)),
                LocalDateTime.of(appointment.getStartDate().toLocalDate(), appointment.getStartDate().toLocalTime().withMinute(59)));

        if(appointment.getDoctorId() != null) {
            int appointmentPerHour = findMaxDoctorAppointmentPerHour(clinicCalendar, appointment.getDoctorId(), appointment.getStartDate());
            List<Appointment> doctorAppointments = appointmentDatabaseService.findAppointmentByClinicIdAndDoctorId(appointment.getClinicId(), appointment.getDoctorId(),
                    LocalDateTime.of(appointment.getStartDate().toLocalDate(), appointment.getStartDate().toLocalTime().withMinute(0)),
                    LocalDateTime.of(appointment.getStartDate().toLocalDate(), appointment.getStartDate().toLocalTime().withMinute(59)));

            if (doctorAppointments.size() >= appointmentPerHour) {
                logger.info("Patient cannot create appointment due to doctor["+appointment.getDoctorId()+"] time slot is full[" + appointments.size() + "]["+appointmentPerHour+"]");
                timeSlotAvailable = false;
            }
            if(appointments.size() >= maxClinicAppointmentPerHour){
                logger.info("Patient cannot create appointment due to clinic time slot is full[" + appointments.size() + "]["+maxClinicAppointmentPerHour+"]");
                timeSlotAvailable = false;
            }
        }else{

            if(appointments.size() >= maxClinicAppointmentPerHour){
                logger.info("Patient cannot create appointment due to clinic time slot is full[" + appointments.size() + "]["+maxClinicAppointmentPerHour+"]");
                timeSlotAvailable = false;
            }
        }

        if (!(withinClinicHours && doctorAvailable && timeSlotAvailable)) {
            throw new RuntimeException("Appointment time is not valid clinicCalendar[" + clinicCalendar
                    + "] doctorCalendar[" + doctorAvailable + "]");
        } else {
            logger.info("saving appointment");
            return appointmentDatabaseService.saveAppointment(appointment);
        }
    }

    public boolean deleteDoctorAppointment(String appointmentId){
        logger.info("Delete a appointment [" + appointmentId + "]");
        return appointmentDatabaseService.removeDoctorCalendar(appointmentId);
    }

    public List<Appointment> searchAppointment(List<String> clinicIds, List<String> doctorIds,
                                               boolean containNoDoctorPreferred, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Search appointment for clinics [" + clinicIds + "], doctors ["+doctorIds+"], " +
                "from startDate["+startDate+"] to endDate["+endDate+"] with no doctor perferred["+containNoDoctorPreferred+"]");
        List<Appointment> appointments = appointmentDatabaseService.findAppointmentByClinicIdsOrDoctorIds(clinicIds, doctorIds, startDate, endDate);
        List<Appointment> returnAppointments = null;
        if(!containNoDoctorPreferred) {
            returnAppointments = appointments.stream().filter(appointment -> appointment.getDoctorId() != null).collect(Collectors.toList());
        }
        if(returnAppointments == null) {
            returnAppointments = appointments;
        }

        return returnAppointments;
    }

    public List<Appointment> listConflictAppointment(String clinicId) {
        logger.info("List conflict appointment by clinic["+clinicId+"]");
        List<Appointment> appointments = appointmentDatabaseService.findAppointmentByClinicId(clinicId, LocalDateTime.now(), LocalDateTime.MAX);

        return appointments.stream()
                .filter(this::checkAppointmentConflict)
                .collect(Collectors.toList());
    }

    public ClinicCalendar findClinicCalendar(String clinicId) {
        logger.info("Find clinic calendar by clinic["+clinicId+"]");
        return appointmentDatabaseService.findCalendar(clinicId);
    }

    public DoctorCalendar findDoctorCalendar(String clinicId, String doctorId) {
        logger.info("Find doctor calendar by clinic["+clinicId+"], doctor["+doctorId+"]");
        return appointmentDatabaseService.findDoctorCalendar(clinicId, doctorId);
    }

    public ClinicCalendar updateClinicWorkingHour(String clinicId, List<CalendarTimeSlot> workingHourTimeSlot) {
        logger.info("Add clinic working hour by clinic["+clinicId+"], timeslot["+workingHourTimeSlot+"]");
        ClinicCalendar clinicCalendar = appointmentDatabaseService.findCalendar(clinicId);
        for (CalendarTimeSlot operationHour : workingHourTimeSlot) {
            if (operationHour.getStart() == null || operationHour.getEnd() == null) {
                logger.error("operating hours not available [" + operationHour + "]");
                throw new RuntimeException("Operating hours not set");
            } else if (operationHour.getCalendarDay() == null) {
                logger.error("operating day not set [" + operationHour + "]");
                throw new RuntimeException("Operating day not set");
            }
        }
        clinicCalendar.setOperationHours(workingHourTimeSlot);
        return appointmentDatabaseService.saveClinic(clinicCalendar);
    }

    public ClinicCalendar updateClinicHoliday(String clinicId, List<CalendarTimeSlot> holidayTimeSlot) {
        logger.info("Add clinic holiday by clinic["+clinicId+"], timeslot["+holidayTimeSlot+"]");
        ClinicCalendar clinicCalendar = appointmentDatabaseService.findCalendar(clinicId);
        for (CalendarTimeSlot holiday : holidayTimeSlot) {
            if (holiday.getStart() == null || holiday.getEnd() == null) {
                logger.error("operating hours not available [" + holiday + "]");
                throw new RuntimeException("Operating hours not set");
            } else if (holiday.getCalendarDay() == null) {
                logger.error("operating day not set [" + holiday + "]");
                throw new RuntimeException("Operating day not set");
            }
        }
        clinicCalendar.setClinicHolidays(holidayTimeSlot);
        return appointmentDatabaseService.saveClinic(clinicCalendar);
    }

    public DoctorCalendar updateDoctorLeave(String clinicId, String doctorId, List<CalendarTimeSlot> leaveTimeSlot) {
        logger.info("Add doctor leave by clinic["+clinicId+"], doctor["+doctorId+"], timeslot["+leaveTimeSlot+"]");
        DoctorCalendar doctorCalendar = appointmentDatabaseService.findDoctorCalendar(clinicId, doctorId);
        for (CalendarTimeSlot holiday : leaveTimeSlot) {
            if (holiday.getStart() == null || holiday.getEnd() == null) {
                logger.error("operating hours not available [" + holiday + "]");
                throw new RuntimeException("Operating hours not set");
            } else if (holiday.getCalendarDay() == null) {
                logger.error("operating day not set [" + holiday + "]");
                throw new RuntimeException("Operating day not set");
            }
        }
        doctorCalendar.setLeaves(leaveTimeSlot);
        return appointmentDatabaseService.saveDoctorCalendar(doctorCalendar);
    }

    public DoctorCalendar updateDoctorWorkingDay(String clinicId, String doctorId, List<CalendarTimeSlot> workDayTimeSlot) {
        logger.info("Add doctor working day by clinic["+clinicId+"], doctor["+doctorId+"], timeslot["+workDayTimeSlot+"]");
        DoctorCalendar doctorCalendar = appointmentDatabaseService.findDoctorCalendar(clinicId, doctorId);
        for (CalendarTimeSlot workingDay : workDayTimeSlot) {
            if (workingDay.getStart() == null || workingDay.getEnd() == null) {
                logger.error("operating hours not available [" + workingDay + "]");
                throw new RuntimeException("Operating hours not set");
            } else if (workingDay.getCalendarDay() == null) {
                logger.error("operating day not set [" + workingDay + "]");
                throw new RuntimeException("Operating day not set");
            }
        }
        doctorCalendar.setLeaves(workDayTimeSlot);
        return appointmentDatabaseService.saveDoctorCalendar(doctorCalendar);
    }

    /*public DoctorCalendar updateDoctorBlockTime(String clinicId, String doctorId, List<CalendarTimeSlot> blockTimeSlot) {
        logger.info("Add doctor block time by clinic["+clinicId+"], doctor["+doctorId+"], timeslot["+blockTimeSlot+"]");
        DoctorCalendar doctorCalendar = calendarDatabaseService.findDoctorCalendar(clinicId, doctorId);
        for (CalendarTimeSlot workingDay : workDayTimeSlot) {
            if (workingDay.getStart() == null || workingDay.getEnd() == null) {
                logger.error("operating hours not available [" + workingDay + "]");
                throw new RuntimeException("Operating hours not set");
            } else if (workingDay.getCalendarDay() == null) {
                logger.error("operating day not set [" + workingDay + "]");
                throw new RuntimeException("Operating day not set");
            }
        }
        doctorCalendar.setLeaves(workDayTimeSlot);
        return calendarDatabaseService.saveDoctorCalendar(doctorCalendar);
    }*/

    private boolean checkAppointmentConflict(Appointment appointment) {
        ClinicCalendar clinicCalendar = appointmentDatabaseService.findCalendar(appointment.getClinicId());
        DoctorCalendar doctorCalendar = appointmentDatabaseService.findDoctorCalendar(appointment.getClinicId(), appointment.getDoctorId());

        boolean withinClinicHours = clinicCalendar.isWithinClinicHours(appointment.getStartDate());
        boolean doctorAvailable = true;
        if (doctorCalendar != null) {
            doctorAvailable = doctorCalendar.isDoctorAvailable(appointment.getStartDate());
        }

        int maxClinicAppointmentPerHour = findMaxClinicAppointmentPerHour(clinicCalendar, appointment.getStartDate());

        List<Appointment> appointments = appointmentDatabaseService.findAppointmentByClinicId(appointment.getClinicId(),
                LocalDateTime.of(appointment.getStartDate().toLocalDate(), appointment.getStartDate().toLocalTime().withMinute(0)),
                LocalDateTime.of(appointment.getStartDate().toLocalDate(), appointment.getStartDate().toLocalTime().withMinute(59)));

        boolean timeSlotAvailable = appointments.size() >= maxClinicAppointmentPerHour;

        if (!(withinClinicHours && doctorAvailable && timeSlotAvailable)) {
            logger.info("check appointment["+appointment+"] has conflict time clinicCalendar[" + clinicCalendar
                    + "] doctorCalendar[" + doctorAvailable + "] clinicTimeSlotAvailable ["+maxClinicAppointmentPerHour+"]");
            return true;
        }
        return false;

    }

    private int findMaxClinicAppointmentPerHour(ClinicCalendar clinicCalendar, LocalDateTime dateTime){
        List<DoctorCalendar> doctorCalendars = appointmentDatabaseService.findDoctorCalendarByClinic(clinicCalendar.getClinicId());

        List<DoctorCalendar> availableDoctorCalendars = doctorCalendars.stream()
                .filter(doctorCalendar1 -> doctorCalendar1.isDoctorAvailable(dateTime))
                .collect(Collectors.toList());
        int noOfDoctorInClinic = availableDoctorCalendars.size();
        if(clinicCalendar.getAvgConsultationTime() == 0){
            logger.error("Clinic["+clinicCalendar.getClinicId()+"] consultation time has not set");
        }
        return new BigDecimal(60 / clinicCalendar.getAvgConsultationTime() * noOfDoctorInClinic).intValue();
    }

    private int findMaxDoctorAppointmentPerHour(ClinicCalendar clinicCalendar, String doctorId, LocalDateTime dateTime){
        DoctorCalendar doctorCalendar = appointmentDatabaseService.findDoctorCalendar(clinicCalendar.getClinicId(), doctorId);
        int noOfDoctorInClinic = 0;
        if(doctorCalendar.isDoctorAvailable(dateTime)){
            noOfDoctorInClinic = 1;
        }
        if(clinicCalendar.getAvgConsultationTime() == 0){
            logger.error("Clinic["+clinicCalendar.getClinicId()+"] consultation time has not set");
        }
        return new BigDecimal(60 / clinicCalendar.getAvgConsultationTime() * noOfDoctorInClinic).intValue();
    }


}
