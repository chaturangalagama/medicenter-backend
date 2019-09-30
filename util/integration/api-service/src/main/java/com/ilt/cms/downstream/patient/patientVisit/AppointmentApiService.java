package com.ilt.cms.downstream.patient.patientVisit;

import com.ilt.cms.api.entity.calendar.AppointmentEntity;
import com.ilt.cms.api.entity.calendar.CalendarTimeSlotEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface AppointmentApiService {

    ResponseEntity<ApiResponse> createAppointment(AppointmentEntity appointmentEntity);

    ResponseEntity<ApiResponse> modifyAppointment(String appointmentId, AppointmentEntity appointmentEntity);

    ResponseEntity<ApiResponse> deleteAppointment(String appointmentId);

    ResponseEntity<ApiResponse> searchAppointment(List<String> clinicIds, List<String> doctorIds, boolean containNoDoctorPreferred, LocalDate startDate, LocalDate endDate);

    ResponseEntity<ApiResponse> listClinicAppointment(String clinicId, LocalDate startDate, LocalDate endDate);

    ResponseEntity<ApiResponse> listDoctorAppointment(String doctorId, LocalDate startDate, LocalDate endDate);

    ResponseEntity<ApiResponse> listConflictAppointment(String clinicId);

    ResponseEntity<ApiResponse> findClinicCalendar(String clinicId);

    ResponseEntity<ApiResponse> findDoctorCalendar(String clinicId, String doctorId);

    ResponseEntity<ApiResponse> updateClinicWorkingHour(String clinicId, List<CalendarTimeSlotEntity> workingHourTimeSlotEntity);

    ResponseEntity<ApiResponse> updateClinicHoliday(String clinicId, List<CalendarTimeSlotEntity> holidayTimeSlotEntity);

    ResponseEntity<ApiResponse> updateDoctorLeave(String clinicId, String doctorId, List<CalendarTimeSlotEntity> leaveTimeSlotEntity);

    ResponseEntity<ApiResponse> updateDoctorWorkingDay(String clinicId, String doctorId, List<CalendarTimeSlotEntity> leaveTimeSlotEntity);

    //ResponseEntity<ApiResponse> updateDoctorBlockTime(String clinicId, String doctorId, List<CalendarTimeSlotEntity> blockTimeSlotEntity);

    ResponseEntity<ApiResponse> removeDoctorBlockTime(String clinicId, String doctorId, LocalDateTime dateTime);

    ResponseEntity<ApiResponse> addDoctorBlockTime(String clinicId, String doctorId, LocalDate date, LocalTime start, LocalTime end);
}
