package com.ilt.cms.pm.rest.controller.patient.patientVisit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.calendar.AppointmentEntity;
import com.ilt.cms.api.entity.calendar.CalendarTimeSlotEntity;
import com.ilt.cms.downstream.patient.patientVisit.AppointmentApiService;
import com.lippo.cms.util.CMSConstant;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/appointment")
//@RolesAllowed("ROLE_APPOINTMENT")
public class AppointmentRestController {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentRestController.class);

    private final String CLINIC_ID_LIST = "";

    private final String DOCTOR_ID_LIST = "";

    private AppointmentApiService appointmentApiService;

    public AppointmentRestController(AppointmentApiService appointmentApiService) {
        this.appointmentApiService = appointmentApiService;
    }

    @PostMapping("/create")
    public HttpEntity<ApiResponse> createAppointment(@RequestBody AppointmentEntity appointmentEntity) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Create appointment  [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.createAppointment(appointmentEntity);
        return serviceResponse;
    }

    @PostMapping("/update/{appointmentId}")
    public HttpEntity<ApiResponse> modifyAppointment(@PathVariable("appointmentId") String appointmentId,
                                                     @RequestBody AppointmentEntity appointmentEntity) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Update appointment  [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.modifyAppointment(appointmentId, appointmentEntity);
        return serviceResponse;
    }

    @PostMapping("/remove/{appointmentId}")
    public HttpEntity<ApiResponse> deleteAppointment(@PathVariable("appointmentId") String appointmentId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Delete appointment  [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.deleteAppointment(appointmentId);
        return serviceResponse;
    }

    @PostMapping("/search/{startDate}/{endDate}/{noDoctorPreferred}")
    public HttpEntity<ApiResponse> searchAppointment(@RequestBody Map<String, List<String>> idsMap,
                                                     @PathVariable("noDoctorPreferred") boolean containNoDoctorPreferred,
                                                     @PathVariable("startDate")
                                                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
                                                     @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
                                                             LocalDate startDate,
                                                     @PathVariable("endDate")
                                                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
                                                     @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
                                                             LocalDate endDate) {
        logger.info("Search appointment []");
        List<String> clinicIds = idsMap.get(CLINIC_ID_LIST);
        List<String> doctorIds = idsMap.get(DOCTOR_ID_LIST);
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.searchAppointment(clinicIds,
                doctorIds, containNoDoctorPreferred, startDate, endDate);
        return serviceResponse;
    }

    @PostMapping("/list/clinic/{clinicId}/{startDate}/{endDate}")
    public HttpEntity<ApiResponse> listClinicAppointment(@PathVariable("clinicId") String clinicId,
                                                         @PathVariable("startDate")
                                                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
                                                         @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
                                                                 LocalDate startDate,
                                                         @PathVariable("endDate")
                                                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
                                                         @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
                                                                 LocalDate endDate) {
        logger.info("List clinic appointment []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.listClinicAppointment(clinicId, startDate, endDate);
        return serviceResponse;
    }

    @PostMapping("/list/doctor/{doctorId}/{startDate}/{endDate}")
    public HttpEntity<ApiResponse> listDoctorAppointment(@PathVariable("doctorId") String doctorId,
                                                         @PathVariable("startDate")
                                                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
                                                         @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
                                                                 LocalDate startDate,
                                                         @PathVariable("endDate")
                                                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
                                                         @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
                                                                 LocalDate endDate) {
        logger.info("List doctor appointment []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.listDoctorAppointment(doctorId, startDate, endDate);
        return serviceResponse;
    }

    @PostMapping("/list/conflict/{clinicId}")
    public HttpEntity<ApiResponse> listConflictAppointment(@PathVariable("clinicId") String clinicId) {
        logger.info("List conflict appointment []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.listConflictAppointment(clinicId);
        return serviceResponse;
    }

    @PostMapping("/find/calendar/{clinicId}")
    public HttpEntity<ApiResponse> findClinicCalendar(@PathVariable("clinicId") String clinicId) {
        logger.info("Find clinic calendar []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.findClinicCalendar(clinicId);
        return serviceResponse;
    }

    @PostMapping("/find/calendar/{clinicId}/{doctorId}")
    public HttpEntity<ApiResponse> findDoctorCalendar(@PathVariable("clinicId") String clinicId,
                                                      @PathVariable("doctorId") String doctorId) {
        logger.info("Find doctor calendar []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.findDoctorCalendar(clinicId, doctorId);
        return serviceResponse;

    }

    @PostMapping("/update/work-hour/{clinicId}")
    public HttpEntity<ApiResponse> updateClinicWorkingHour(@PathVariable("clinicId") String clinicId,
                                                           @RequestBody List<CalendarTimeSlotEntity> workingHourTimeSlots) {
        logger.info("Add clinic working hour []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.updateClinicWorkingHour(clinicId, workingHourTimeSlots);
        return serviceResponse;
    }

    @PostMapping("/update/holiday/{clinicId}")
    public HttpEntity<ApiResponse> updateClinicHoliday(@PathVariable("clinicId") String clinicId,
                                                       @RequestBody List<CalendarTimeSlotEntity> holidayTimeSlots) {
        logger.info("Add clinic Holiday []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.updateClinicHoliday(clinicId, holidayTimeSlots);
        return serviceResponse;

    }

    @PostMapping("/update/leave/{clinicId}/{doctorId}")
    public HttpEntity<ApiResponse> updateDoctorLeave(@PathVariable("clinicId") String clinicId,
                                                     @PathVariable("doctorId") String doctorId,
                                                     @RequestBody List<CalendarTimeSlotEntity> leaveTimeSlots) {
        logger.info("Add doctor leave []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.updateDoctorLeave(clinicId, doctorId, leaveTimeSlots);
        return serviceResponse;

    }

    @PostMapping("/update/work-day/{clinicId}/{doctorId}")
    public HttpEntity<ApiResponse> updateDoctorWorkingDay(@PathVariable("clinicId") String clinicId,
                                                          @PathVariable("doctorId") String doctorId,
                                                          @RequestBody List<CalendarTimeSlotEntity> workingDayTimeSlots) {
        logger.info("Add doctor working day []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.updateDoctorWorkingDay(clinicId, doctorId, workingDayTimeSlots);
        return serviceResponse;
    }

    @PostMapping("/add/block-time/{clinicId}/{doctorId}/{date}/{start}/{end}")
    public HttpEntity<ApiResponse> addDoctorBlockTime(@PathVariable("clinicId") String clinicId,
                                                      @PathVariable("doctorId") String doctorId,
                                                      @PathVariable("date")
                                                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT)
                                                      @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
                                                              LocalDate date,
                                                      @PathVariable("start")
                                                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_TIME_FORMAT)
                                                      @DateTimeFormat(pattern = CMSConstant.JSON_TIME_FORMAT)
                                                              LocalTime start,
                                                      @PathVariable("end")
                                                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_TIME_FORMAT)
                                                      @DateTimeFormat(pattern = CMSConstant.JSON_TIME_FORMAT)
                                                              LocalTime end) {
        logger.info("Add doctor block time []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.addDoctorBlockTime(clinicId, doctorId, date, start, end);
        return serviceResponse;
    }

    @PostMapping("/remove/block-time/{clinicId}/{doctorId}/{dateTime}")
    public HttpEntity<ApiResponse> removeDoctorBlockTime(@PathVariable("clinicId") String clinicId,
                                                         @PathVariable("doctorId") String doctorId,
                                                         @PathVariable("dateTime")
                                                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT_WITH_HOUR)
                                                         @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_HOUR)
                                                                 LocalDateTime dateTime) {
        logger.info("Add doctor block time []");
        ResponseEntity<ApiResponse> serviceResponse = appointmentApiService.removeDoctorBlockTime(clinicId, doctorId, dateTime);
        return serviceResponse;
    }

    /*@PostMapping("/list/appointment/doctor/{doctorId}/{startDate}/{endDate}")
    public HttpEntity<ApiResponse> listDoctorAppointment(@PathVariable("doctorId") String doctorId,
                                        @PathVariable("startDate")
                                        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate startDate,
                                        @PathVariable("endDate")
                                        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate endDate) {
        logger.info("list appointment by doctor["+doctorId+"]");
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.listDoctorAppointment(doctorId, startDate, endDate);
        return serviceResponse;
    }

    @PostMapping("/list/appointment/clinic/{clinicId}/{startDate}/{endDate}")
    public HttpEntity<ApiResponse> listClinicAppointment(@PathVariable("clinicId") String clinicId,
                                                         @PathVariable("startDate")
                                                         @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate startDate,
                                                         @PathVariable("endDate")
                                                         @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate endDate) {
        logger.info("list appointment by clinic["+clinicId+"]");
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.listClinicAppointment(clinicId, startDate, endDate);
        return serviceResponse;
    }

    @PostMapping("/search/appointment/{containNoDoctorPreferred}/{startDate}/{endDate}")
    public HttpEntity<ApiResponse> searchAppointment(@RequestBody Map<String, List<String>> idMap,
                                                     @PathVariable("containNoDoctorPreferred") boolean containNoDoctorPreferred,
                                                     @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) @PathVariable("startDate") LocalDate startDate,
                                                     @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) @PathVariable("endDate") LocalDate endDate){
        ResponseEntity<ApiResponse> serviceResponse  = appointmentDownstream.searchAppointment(idMap, containNoDoctorPreferred, startDate, endDate);
        return serviceResponse;
    }

    @PostMapping("/list/block-doctor-time/{clinicId}/{startDate}/{endDate}")
    public HttpEntity<ApiResponse> listBlockDoctorTime(@PathVariable("clinicId") String clinicId,
                                                       @PathVariable("startDate") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate startDate,
                                                       @PathVariable("endDate") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate endDate) {
        logger.info("list block doctor time by clinic["+clinicId+"] from startDate["+startDate+"] to endDate["+endDate+"]");
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.listBlockDoctorTime(clinicId, startDate, endDate);
        return serviceResponse;

    }

    @PostMapping("/list/holiday/{clinicId}/{startDate}/{endDate}")
    public HttpEntity<ApiResponse> listClinicHoliday(@PathVariable("clinicId") String clinicId,
                                                     @PathVariable("startDate") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate startDate,
                                                     @PathVariable("endDate") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate endDate) {
        logger.info("list holiday by clinic["+clinicId+"] from startDate["+startDate+"] to endDate["+endDate+"]");
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.listClinicHoliday(clinicId, startDate, endDate);
        return serviceResponse;
    }

    @PostMapping("/list/ramco-leave/{doctorId}/{startDate}/{endDate}")
    public HttpEntity<ApiResponse> listDoctorRamcoLeave(@PathVariable("doctorId") String doctorId,
                                                     @PathVariable("startDate") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate startDate,
                                                     @PathVariable("endDate") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate endDate) {
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.listDoctorRamcoLeave(doctorId, startDate, endDate);
        //ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.listDoctorRamcoLeave(doctorId, LocalDate.now(), LocalDate.now());
        return serviceResponse;
    }

    @PostMapping("/create/holiday/{holidayCode}/{description}/{date}")
    public HttpEntity<ApiResponse> createHoliday(@PathVariable("holidayCode") String holidayCode,
                                                 @PathVariable("description") String description,
                                                 @PathVariable("date") @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT) LocalDate date) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        logger.info("Create HolidayEntity [" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.createHoliday(holidayCode, description, date);
        return serviceResponse;
    }

    @PostMapping("/add/holiday/{clinicId}/{holidayCode}")
    public HttpEntity<ApiResponse> addHoliday(@PathVariable("clinicId") String clinicId,
                                              @PathVariable("holidayCode") String holidayCode) {
        logger.info("Add holiday["+holidayCode+"] to clinic["+clinicId+"]");
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.addHoliday(clinicId, holidayCode);
        return serviceResponse;
    }

    @PostMapping("/remove/holiday/{clinicId}/{holidayCode}")
    public HttpEntity<ApiResponse> removeHoliday(@PathVariable("clinicId") String clinicId,
                                                     @PathVariable("holidayCode") String holidayCode) {
        logger.info("Remove holiday["+holidayCode+"] from clinic["+clinicId+"]");
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.removeHoliday(clinicId, holidayCode);
        return serviceResponse;
    }

    @PostMapping("/create/block-doctor-time")
    public HttpEntity<ApiResponse> createBlockDotorTime(@RequestBody BlockDoctorTimeEntity blockDoctorTimeEntity){
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.createBlockDoctorTime(blockDoctorTimeEntity);
        return serviceResponse;
    }

    @PostMapping("/remove/block-doctor-time/{blockDoctorTimeId}")
    public HttpEntity<ApiResponse> removeBlockDotorTime(@PathVariable("blockDoctorTimeId")String blockDoctorTimeId){
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.removeBlockDoctorTime(blockDoctorTimeId);
        return serviceResponse;
    }

    //TODO will be remove
    @PostMapping("/create/clinic-work-hour")
    public HttpEntity<ApiResponse> createClinicWorkHour(@RequestBody ClinicWorkHourEntity clinicWorkHourEntity){
        ResponseEntity<ApiResponse> serviceResponse = appointmentDownstream.createClinicWorkHour(clinicWorkHourEntity);
        return serviceResponse;
    }*/

}
