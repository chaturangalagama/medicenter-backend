package business.mock;

import com.ilt.cms.core.entity.calendar.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class MockAppointment {

    public static Appointment mockAppointment(){
        return mockAppointment(null, "P000001", "D000001", "C000001", "test", "This is Remark",
                LocalDateTime.of(2018, 10, 31, 23, 0, 0),
                LocalDateTime.of(2018, 10, 30, 17, 0,0),
                120);
    }

    public static Appointment mockAppointment(String id, String patientId, String doctorId, String clinicId, String appointmentPurpose,
                                                    String remark, LocalDateTime startDate,
                                              LocalDateTime remindTime, int duration){
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setPatientId(patientId);
        appointment.setDoctorId(doctorId);
        appointment.setClinicId(clinicId);
        appointment.setAppointmentPurpose(appointmentPurpose);
        appointment.setRemark(remark);
        appointment.setStartDate(startDate);
        appointment.setReminderDate(remindTime);
        appointment.setDuration(duration);
        return appointment;
    }

    public CalendarDayOfWeek mockCalendarDayOfWeek(DayOfWeek dayOfWeek){
        CalendarDayOfWeek calendarDayOfWeek = new CalendarDayOfWeek(dayOfWeek);

        return calendarDayOfWeek;
    }

    public CalendarDayOfYear mockCalendarDayOfYear(LocalDate date){
        CalendarDayOfYear calendarDayOfYear = new CalendarDayOfYear(date);

        return calendarDayOfYear;
    }

    public CalendarTimeSlot mockCalendarTimeSlotOfWeek(CalendarDayOfWeek calendarDay, LocalTime startTime, LocalTime endTime){
        CalendarTimeSlot calendarTimeSlot = new CalendarTimeSlot(calendarDay, startTime, endTime);
        return calendarTimeSlot;
    }

    public CalendarTimeSlot mockCalendarTimeSlotOfYearEntity(CalendarDayOfYear calendarDay, LocalTime startTime, LocalTime endTime){
        CalendarTimeSlot calendarTimeSlot = new CalendarTimeSlot(calendarDay, startTime, endTime);
        return calendarTimeSlot;
    }

    public ClinicCalendar mockClinicCalendar(String id, String clinicId, int avgConsultationTime, int maxWaitTime, List<CalendarTimeSlot> workingHour,
                                                          List<CalendarTimeSlot> holiday){
        ClinicCalendar clinicCalendar = new ClinicCalendar();
        clinicCalendar.setId(id);
        clinicCalendar.setClinicId(clinicId);
        clinicCalendar.setAvgConsultationTime(avgConsultationTime);
        clinicCalendar.setMaxWaitTime(maxWaitTime);
        clinicCalendar.setOperationHours(workingHour);
        clinicCalendar.setClinicHolidays(holiday);
        return clinicCalendar;
    }

    public DoctorCalendar mockDoctorCalendar(String id, String clinicId, String doctorId, List<CalendarTimeSlot> workingDays,
                                                          List<CalendarTimeSlot> leaves, List<CalendarTimeSlot> blockedTime){
        DoctorCalendar doctorCalendar = new DoctorCalendar();
        doctorCalendar.setId(id);
        doctorCalendar.setClinicId(clinicId);
        doctorCalendar.setDoctorId(doctorId);
        doctorCalendar.setWorkingDays(workingDays);
        doctorCalendar.setLeaves(leaves);
        doctorCalendar.setBlockedTime(blockedTime);
        return doctorCalendar;
    }

}
