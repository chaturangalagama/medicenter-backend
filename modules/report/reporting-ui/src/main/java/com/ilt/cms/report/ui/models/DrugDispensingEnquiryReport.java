package com.ilt.cms.report.ui.models;

import com.ilt.cms.report.ui.models.dataset.CommonDataSet;
import com.ilt.cms.report.ui.service.DrugDispensingEnquiryReportService;
import com.ilt.cms.report.ui.util.Formatter;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DrugDispensingEnquiryReport extends CommonDataSet implements IPojoDataSet, Serializable {

    private String uom;
    private Float quantity;

    private Iterator<DrugDispensingEnquiryReport> iterator;

    public DrugDispensingEnquiryReport() {
    }

    public DrugDispensingEnquiryReport(Date startTime, String doctorName, String patientName,
                                       int age, String patientGender, String patientContactNumber,
                                       String patientNumber, Float quantity, String uom) {
        setStartTime(startTime);
        setDoctorName(doctorName);
        setPatientName(patientName);
        setPatientAge(age);
        setPatientGender(patientGender);
        setPatientContactNumber(patientContactNumber);
        setPatientNumber(patientNumber);
        this.quantity = quantity;
        this.uom = uom;
    }

    @Override
    public void open(Object o, Map<String, Object> map) throws OdaException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(String.valueOf(map.get("startDate")), formatter);
        LocalDate endDate = LocalDate.parse(String.valueOf(map.get("endDate")), formatter);
        List<String> doctorIds = Formatter.asList(String.valueOf(map.get("doctorIds")));
        String drugCode = String.valueOf(map.get("drugCode"));

        iterator = new DrugDispensingEnquiryReportService()
                .getPatientDrugDispenseData(doctorIds,
                        LocalDateTime.of(startDate, LocalTime.MIN),
                        LocalDateTime.of(endDate, LocalTime.MAX), drugCode)
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

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
}
