package com.ilt.cms.pm.integration.impl.patient.patientVisit;

import com.ilt.cms.api.entity.calendar.AppointmentEntity;
import com.ilt.cms.api.entity.calendar.CalendarTimeSlotEntity;
import com.ilt.cms.core.entity.calendar.Appointment;
import com.ilt.cms.core.entity.calendar.ClinicCalendar;
import com.ilt.cms.core.entity.calendar.DoctorCalendar;
import com.ilt.cms.downstream.patient.patientVisit.AppointmentApiService;
import com.ilt.cms.pm.business.service.patient.patientVisit.AppointmentService;
import com.ilt.cms.pm.integration.mapper.patient.patientVisit.AppointmentMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultAppointmentApiService implements AppointmentApiService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultAppointmentApiService.class);

    private AppointmentService appointmentService;

    public DefaultAppointmentApiService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public ResponseEntity<ApiResponse> createAppointment(AppointmentEntity appointmentEntity) {
        try {
            Appointment doctorAppointment = appointmentService.createDoctorAppointment(AppointmentMapper.mapToCore(appointmentEntity));
            return httpApiResponse(new HttpApiResponse(doctorAppointment));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> modifyAppointment(String appointmentId, AppointmentEntity appointmentEntity) {
        try {
            Appointment appointment = appointmentService.modifyDoctorAppointment(appointmentId, AppointmentMapper.mapToCore(appointmentEntity));
            return httpApiResponse(new HttpApiResponse(AppointmentMapper.mapToEntity(appointment)));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deleteAppointment(String appointmentId) {
        appointmentService.deleteDoctorAppointment(appointmentId);
        return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
    }

    @Override
    public ResponseEntity<ApiResponse> searchAppointment(List<String> clinicIds, List<String> doctorIds,
                                                         boolean containNoDoctorPreferred, LocalDate startDate, LocalDate endDate) {
        List<Appointment> appointments = appointmentService.searchAppointment(clinicIds, doctorIds, containNoDoctorPreferred,
                startDate.atTime(0,0), endDate.atTime(23, 59));
        return httpApiResponse(new HttpApiResponse(appointments.stream()
                .map(AppointmentMapper::mapToEntity)
                .collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<ApiResponse> listClinicAppointment(String clinicId, LocalDate startDate, LocalDate endDate) {
        List<Appointment> appointments = appointmentService.findClinicAppointments(clinicId,
                LocalDateTime.of(startDate, LocalTime.of(0, 0, 0)),
                LocalDateTime.of(endDate, LocalTime.of(23, 59, 59)));
        return httpApiResponse(new HttpApiResponse(appointments.stream()
                .map(AppointmentMapper::mapToEntity).collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<ApiResponse> listDoctorAppointment(String doctorId, LocalDate startDate, LocalDate endDate) {
        List<Appointment> appointments = appointmentService.findDoctorAppointments(doctorId,
                LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                LocalDateTime.of(endDate, LocalTime.of(23, 59, 59)));
        return httpApiResponse(new HttpApiResponse(appointments.stream()
                .map(AppointmentMapper::mapToEntity).collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<ApiResponse> listConflictAppointment(String clinicId) {
         List<Appointment> appointments = appointmentService.listConflictAppointment(clinicId);
        return httpApiResponse(new HttpApiResponse(appointments.stream()
                .map(AppointmentMapper::mapToEntity).collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<ApiResponse> findClinicCalendar(String clinicId) {
        ClinicCalendar clinicCalendar = appointmentService.findClinicCalendar(clinicId);
        return httpApiResponse(new HttpApiResponse(AppointmentMapper.mapToEntity(clinicCalendar)));
    }

    @Override
    public ResponseEntity<ApiResponse> findDoctorCalendar(String clinicId, String doctorId) {
        DoctorCalendar doctorCalendar = appointmentService.findDoctorCalendar(clinicId, doctorId);
        return httpApiResponse(new HttpApiResponse(AppointmentMapper.mapToEntity(doctorCalendar)));
    }

    @Override
    public ResponseEntity<ApiResponse> updateClinicWorkingHour(String clinicId, List<CalendarTimeSlotEntity> workingHourTimeSlotEntity) {
        ClinicCalendar clinicCalendar = appointmentService.updateClinicWorkingHour(clinicId, workingHourTimeSlotEntity.stream()
                .map(AppointmentMapper::mapToCore)
                .collect(Collectors.toList()));
        return httpApiResponse(new HttpApiResponse(AppointmentMapper.mapToEntity(clinicCalendar)));
    }

    @Override
    public ResponseEntity<ApiResponse> updateClinicHoliday(String clinicId, List<CalendarTimeSlotEntity> holidayTimeSlotEntity) {
        ClinicCalendar clinicCalendar = appointmentService.updateClinicHoliday(clinicId, holidayTimeSlotEntity.stream()
                .map(AppointmentMapper::mapToCore)
                .collect(Collectors.toList()));
        return httpApiResponse(new HttpApiResponse(AppointmentMapper.mapToEntity(clinicCalendar)));
    }

    @Override
    public ResponseEntity<ApiResponse> updateDoctorLeave(String clinicId, String doctorId, List<CalendarTimeSlotEntity> leaveTimeSlotEntity) {
        DoctorCalendar doctorCalendar = appointmentService.updateDoctorLeave(clinicId, doctorId,
                leaveTimeSlotEntity.stream().
                        map(AppointmentMapper::mapToCore)
                        .collect(Collectors.toList()));
        return httpApiResponse(new HttpApiResponse(AppointmentMapper.mapToEntity(doctorCalendar)));
    }


    @Override
    public ResponseEntity<ApiResponse> updateDoctorWorkingDay(String clinicId, String doctorId, List<CalendarTimeSlotEntity> leaveTimeSlotEntity) {
        DoctorCalendar doctorCalendar = appointmentService.updateDoctorWorkingDay(clinicId, doctorId,
                leaveTimeSlotEntity.stream()
                        .map(AppointmentMapper::mapToCore)
                        .collect(Collectors.toList()));
        return httpApiResponse(new HttpApiResponse(AppointmentMapper.mapToEntity(doctorCalendar)));
    }

    @Override
    public ResponseEntity<ApiResponse> removeDoctorBlockTime(String clinicId, String doctorId, LocalDateTime dateTime) {
        boolean isSuccess = appointmentService.removeBlockDoctorTime(clinicId, doctorId, dateTime);
        if(isSuccess){
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        }
        return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
    }

    @Override
    public ResponseEntity<ApiResponse> addDoctorBlockTime(String clinicId, String doctorId, LocalDate date, LocalTime start, LocalTime end) {
        DoctorCalendar doctorCalendar = appointmentService.addBlockDoctorTime(clinicId, doctorId, LocalDateTime.of(date, start), LocalDateTime.of(date, end));
        return httpApiResponse(new HttpApiResponse(AppointmentMapper.mapToEntity(doctorCalendar)));
    }

}
