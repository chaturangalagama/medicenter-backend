package com.ilt.cms.report.ui.models;

import com.ilt.cms.report.ui.models.dataset.CommonDataSet;
import com.ilt.cms.report.ui.service.MedicalCertificateReportService;
import com.ilt.cms.report.ui.util.Formatter;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MedicalCertificateReport extends CommonDataSet implements IPojoDataSet {

    private String purpose;
    private Double numberOfDays;
    private String halfDay;
    private String icd10Term;

    public MedicalCertificateReport() {
    }

    public MedicalCertificateReport(LocalDateTime consulatationStartTime, String doctorName, String patientIdentity, String patientName, String patientNumber, String visitNumber, LocalDate startDate, String purpose, Double numberOfDays, String halfDay, String icd10Term, LocalDate toDate) {
        setStartTime(Date.from(consulatationStartTime.atZone(ZoneId.systemDefault()).toInstant()));
        setDoctorName(doctorName);
        feedPatientData(patientName, patientNumber, patientIdentity);
        setVisitNumber(visitNumber);
        setFromDate(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        this.purpose = purpose;
        this.numberOfDays = numberOfDays;
        this.halfDay = halfDay;
        this.icd10Term = icd10Term;
        setToDate(Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    private Iterator<MedicalCertificateReport> iterator;

    @Override
    public void open(Object o, Map<String, Object> map) throws OdaException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(String.valueOf(map.get("startDate")), formatter);
        LocalDate endDate = LocalDate.parse(String.valueOf(map.get("endDate")), formatter);
        List<String> clinicIds = Formatter.asList(String.valueOf(map.get("clinicIds")));
        List<String> doctorIds = Formatter.asList(String.valueOf(map.get("doctorIds")));

        iterator = new MedicalCertificateReportService()
                .getMedicalCertificateReport(doctorIds, clinicIds, LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX))
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Double getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Double numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getHalfDay() {
        return halfDay;
    }

    public void setHalfDay(String halfDay) {
        this.halfDay = halfDay;
    }

    public String getIcd10Term() {
        return icd10Term;
    }

    public void setIcd10Term(String icd10Term) {
        this.icd10Term = icd10Term;
    }
}
