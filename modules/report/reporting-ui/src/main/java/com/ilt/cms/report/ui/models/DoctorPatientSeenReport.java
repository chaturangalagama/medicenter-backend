package com.ilt.cms.report.ui.models;

import com.ilt.cms.report.ui.service.DoctorPatientSeenReportService;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DoctorPatientSeenReport implements IPojoDataSet {

    private int id;
    private String patientNumber;
    private String visitNumber;
    private LocalDateTime startTime;
    private String patientName;
    private String patientIdentity;
    private String patientAddress;
    private String doctorName;
    private String caseNumber;

    private Iterator<DoctorPatientSeenReport> iterator;

    public DoctorPatientSeenReport() {
    }

    public DoctorPatientSeenReport(int id, String patientNumber, String visitNumber, LocalDateTime startTime,
                                   String patientName, String patientIdentity, String patientAddress, String doctorName, String caseNumber) {
        this.id = id;
        this.patientNumber = patientNumber;
        this.visitNumber = visitNumber;
        this.startTime = startTime;
        this.patientName = patientName;
        this.patientIdentity = patientIdentity;
        this.patientAddress = patientAddress;
        this.doctorName = doctorName;
        this.caseNumber = caseNumber;
    }

    public void open(Object appContext, Map<String, Object> map) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(map.get("startDate").toString(), formatter);
        LocalDate endDate = LocalDate.parse(map.get("endDate").toString(), formatter);
        String doctorIdsString = map.get("doctorIds").toString();
        String[] doctorIds = (doctorIdsString == null || doctorIdsString.length() == 0 || "null".equals(doctorIdsString)) ? new String[0] : doctorIdsString.split(",");

        List<DoctorPatientSeenReport> doctorPatientSeenReport =
                new DoctorPatientSeenReportService().getDoctorPatientSeenReport(doctorIds, startDate, endDate);
        iterator = doctorPatientSeenReport.iterator();
    }

    public Object next() {
        if (iterator.hasNext())
            return iterator.next();
        return null;
    }

    public void close() {
        iterator = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientIdentity() {
        return patientIdentity;
    }

    public void setPatientIdentity(String patientIdentity) {
        this.patientIdentity = patientIdentity;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }
}
