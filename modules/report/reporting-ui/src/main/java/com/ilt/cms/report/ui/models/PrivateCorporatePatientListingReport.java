package com.ilt.cms.report.ui.models;

import com.ilt.cms.report.ui.models.dataset.PrivateCorporatePatientListingItem;
import com.ilt.cms.report.ui.service.PrivateCorporatePatientListingReportService;
import com.ilt.cms.report.ui.util.Formatter;
import org.eclipse.birt.data.oda.pojo.api.IPojoDataSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PrivateCorporatePatientListingReport implements IPojoDataSet {

    private Date startTime;
    private String visitNumber;
    private String patientNumber;
    private String patientName;
    private String patientIdentity;
    private String doctorName;
    private String financialClass;
    private String medicalClass;
    private String coveragePlan;
    private String coveragePlanId;
    private String patientAddress;
    private String diagnosis;
    private List<PrivateCorporatePatientListingItem> items;
    private String dateRange;
    private String invoiceNumber;
    private String paymentStatus;
    private Float totalBillAmount;
    private Float medicalCoveragePayable;
    private Float chasPayable;
    private Float mediSavePayable;
    private Float totalPatientPaid;
    private String clinicCode;
    private String clinicGroup;

    public PrivateCorporatePatientListingReport() {
    }

    public PrivateCorporatePatientListingReport(Date startTime, String visitNumber, String patientNumber,
                                                String patientName, String patientIdentity, String doctorName,
                                                String financialClass, String medicalClass, String coveragePlan,
                                                String coveragePlanId, String patientAddress, String diagnosis,
                                                List<PrivateCorporatePatientListingItem> items, String dateRange,
                                                String invoiceNumber, String paymentStatus, Float totalBillAmount,
                                                Float medicalCoveragePayable, Float chasPayable, Float mediSavePayable,
                                                Float totalPatientPaid, String clinicCode, String clinicGroup) {
        this.startTime = startTime;
        this.visitNumber = visitNumber;
        this.patientNumber = patientNumber;
        this.patientName = patientName;
        this.patientIdentity = patientIdentity;
        this.doctorName = doctorName;
        this.financialClass = financialClass;
        this.medicalClass = medicalClass;
        this.coveragePlan = coveragePlan;
        this.coveragePlanId = coveragePlanId;
        this.patientAddress = patientAddress;
        this.diagnosis = diagnosis;
        this.items = items;
        this.dateRange = dateRange;
        this.invoiceNumber = invoiceNumber;
        this.paymentStatus = paymentStatus;
        this.totalBillAmount = totalBillAmount;
        this.medicalCoveragePayable = medicalCoveragePayable;
        this.chasPayable = chasPayable;
        this.mediSavePayable = mediSavePayable;
        this.totalPatientPaid = totalPatientPaid;
        this.clinicCode = clinicCode;
        this.clinicGroup = clinicGroup;
    }

    private Iterator<PrivateCorporatePatientListingReport> iterator;

    @Override
    public void open(Object o, Map<String, Object> map) throws OdaException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(map.get("startDate").toString(), formatter);
        LocalDate endDate = LocalDate.parse(map.get("endDate").toString(), formatter);
        List<String> doctorIds = Formatter.asList(map.get("doctorIds").toString());
        List<String> clinicIds = Formatter.asList(map.get("clinicIds").toString());
        List<String> statuses = Formatter.asList(map.get("statuses").toString());
        String patientIdentityNumber = map.get("patientIdentityNumber").toString();
        String coverageType = map.get("coverageType").toString();

        iterator = new PrivateCorporatePatientListingReportService().getReportData().iterator();
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getFinancialClass() {
        return financialClass;
    }

    public void setFinancialClass(String financialClass) {
        this.financialClass = financialClass;
    }

    public String getMedicalClass() {
        return medicalClass;
    }

    public void setMedicalClass(String medicalClass) {
        this.medicalClass = medicalClass;
    }

    public String getCoveragePlan() {
        return coveragePlan;
    }

    public void setCoveragePlan(String coveragePlan) {
        this.coveragePlan = coveragePlan;
    }

    public String getCoveragePlanId() {
        return coveragePlanId;
    }

    public void setCoveragePlanId(String coveragePlanId) {
        this.coveragePlanId = coveragePlanId;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<PrivateCorporatePatientListingItem> getItems() {
        return items;
    }

    public void setItems(List<PrivateCorporatePatientListingItem> items) {
        this.items = items;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Float getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(Float totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public Float getMedicalCoveragePayable() {
        return medicalCoveragePayable;
    }

    public void setMedicalCoveragePayable(Float medicalCoveragePayable) {
        this.medicalCoveragePayable = medicalCoveragePayable;
    }

    public Float getChasPayable() {
        return chasPayable;
    }

    public void setChasPayable(Float chasPayable) {
        this.chasPayable = chasPayable;
    }

    public Float getMediSavePayable() {
        return mediSavePayable;
    }

    public void setMediSavePayable(Float mediSavePayable) {
        this.mediSavePayable = mediSavePayable;
    }

    public Float getTotalPatientPaid() {
        return totalPatientPaid;
    }

    public void setTotalPatientPaid(Float totalPatientPaid) {
        this.totalPatientPaid = totalPatientPaid;
    }

    public String getClinicCode() {
        return clinicCode;
    }

    public void setClinicCode(String clinicCode) {
        this.clinicCode = clinicCode;
    }

    public String getClinicGroup() {
        return clinicGroup;
    }

    public void setClinicGroup(String clinicGroup) {
        this.clinicGroup = clinicGroup;
    }

    public String getChargeItems() {
        String items = "";
        for (PrivateCorporatePatientListingItem item : this.getItems()) {
            items = items+(item.getCharge()+"\n");
        }
        return items;
    }

    public String getQuantityItems() {
        String items = "";
        for (PrivateCorporatePatientListingItem item : this.getItems()) {
            items = items+(item.getQuantity().toString()+"\n");
        }
        return items;
    }

    public String getPriceItems() {
        String items = "";
        for (PrivateCorporatePatientListingItem item : this.getItems()) {
            items = items+("$"+item.getPrice().toString()+"\n");
        }
        return items;
    }

}
