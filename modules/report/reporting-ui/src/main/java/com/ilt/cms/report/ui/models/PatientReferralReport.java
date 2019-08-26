package com.ilt.cms.report.ui.models;

import com.ilt.cms.report.ui.models.dataset.CommonDataSet;
import com.ilt.cms.report.ui.service.PatientReferralReportService;
import com.ilt.cms.report.ui.util.Formatter;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PatientReferralReport extends CommonDataSet implements IPojoDataSet {

    private String refClinicCode;
    private String attendingDoctor;
    private String contactNumber;
    private String reason;

    public PatientReferralReport() {
    }

    public PatientReferralReport(String patientNumber, String visitNumber, String refClinicCode, String contactNumber, LocalDateTime startTime, String patientName, String patientIdentity, String attendingDoctor, String clinicCode, String clinicName, String doctorName, String reason) {

        feedPatientData(patientName, patientNumber, patientIdentity);
        feedClinicData(clinicCode, clinicName);
        setDoctorName(doctorName);
        setVisitNumber(visitNumber);
        setStartTime(Formatter.dateConvert(startTime));

        this.refClinicCode = refClinicCode;
        this.contactNumber = contactNumber;
        this.attendingDoctor = attendingDoctor;
        this.reason = reason;
    }

    private Iterator<PatientReferralReport> iterator;

    @Override
    public void open(Object o, Map<String, Object> map) throws OdaException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(String.valueOf(map.get("startDate")), formatter);
        LocalDate endDate = LocalDate.parse(String.valueOf(map.get("endDate")), formatter);
        List<String> doctorIds = Formatter.asList(String.valueOf(map.get("doctorIds")));

        iterator = new PatientReferralReportService()
                .getPatientReferralReport(doctorIds, LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX))
                .iterator();
    }

    @Override
    public Object next() throws OdaException {
        if (iterator.hasNext())
            return iterator.next();
        return null;
    }

    @Override
    public void close() throws OdaException {
        iterator = null;
    }

    public String getRefClinicCode() {
        return refClinicCode;
    }

    public void setRefClinicCode(String refClinicCode) {
        this.refClinicCode = refClinicCode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAttendingDoctor() {
        return attendingDoctor;
    }

    public void setAttendingDoctor(String attendingDoctor) {
        this.attendingDoctor = attendingDoctor;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
