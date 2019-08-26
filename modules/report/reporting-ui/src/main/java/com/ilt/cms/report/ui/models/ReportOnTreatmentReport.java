package com.ilt.cms.report.ui.models;

import com.ilt.cms.report.ui.service.ReportOnTreatmentReportService;
import com.ilt.cms.report.ui.util.Formatter;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReportOnTreatmentReport implements IPojoDataSet {

    private String doctorName;
    private Date startTime;
    private String patientName;
    private String patientNumber;
    private Float totalAmount;
    private String itemCategoryCode;
    private String itemCode;
    private String itemName;
    private String uom;
    private String quantity;

    public ReportOnTreatmentReport() {
    }

    public ReportOnTreatmentReport(String doctorName, Date startTime, String patientName,
                                   String patientNumber, Float totalAmount, String itemCategoryCode,
                                   String itemCode, String itemName, String uom, String quantity) {
        this.doctorName = doctorName;
        this.startTime = startTime;
        this.patientName = patientName;
        this.patientNumber = patientNumber;
        this.totalAmount = totalAmount;
        this.itemCategoryCode = itemCategoryCode;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.uom = uom;
        this.quantity = quantity;
    }

    private Iterator<ReportOnTreatmentReport> iterator;

    @Override
    public void open(Object o, Map<String, Object> map) throws OdaException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(String.valueOf(map.get("startDate")), formatter);
        LocalDate endDate = LocalDate.parse(String.valueOf(map.get("endDate")), formatter);
        List<String> doctorIds = Formatter.asList(String.valueOf(map.get("doctorIds")));
        List<String> clinicIds = Formatter.asList(String.valueOf(map.get("clinicIds")));

        iterator = new ReportOnTreatmentReportService().getReportData().iterator();
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getItemCategoryCode() {
        return itemCategoryCode;
    }

    public void setItemCategoryCode(String itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
