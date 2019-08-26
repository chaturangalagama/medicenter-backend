package com.ilt.cms.report.ui.models.dataset;

import java.util.Date;

public class CommonDataSet {

    private int id;

    private Date startTime;
    private Date endTime;
    private Date fromDate;
    private Date toDate;

    private String coverageName;
    private String coverageType;

    private String clinicCode;
    private String clinicName;

    private String caseNumber;

    private String patientName;
    private String patientNumber;
    private String patientIdentity;
    private String patientGender;
    private String patientAddress;
    private String patientContactNumber;
    private int patientAge;

    private String doctorName;

    private String visitNumber;

    private String itemCategoryCode;
    private String itemCode;
    private String itemName;

    private String invoiceNumber;

    private Float patientPayable;
    private Float corporatePayable;
    private Float insurancePayable;
    private Float chasPayable;
    private Float mediSavePayable;

    private Float chargedAmount;
    private Float totalPatientPaid;
    private Float totalBillAmount;

    public void feedPatientData(String patientName, String patientNumber, String patientIdentity) {
        this.patientName = patientName;
        this.patientNumber = patientNumber;
        this.patientIdentity = patientIdentity;
    }

    public void feedClinicData(String clinicCode, String clinicName) {
        this.clinicCode = clinicCode;
        this.clinicName = clinicName;
    }

    public void feedDoctorData(String doctorName) {
        this.doctorName = doctorName;
    }

    public void feedCaseData(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void feedPatientVisitRegistryData(String visitId) {
        this.visitNumber = visitId;
    }

    public void feedItemData(String itemName, String itemCode, String itemCategoryCode) {
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.itemCategoryCode = itemCategoryCode;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public String getItemCategoryCode() {
        return itemCategoryCode;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(Float totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public String getCoverageName() {
        return coverageName;
    }

    public void setCoverageName(String coverageName) {
        this.coverageName = coverageName;
    }

    public String getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(String coverageType) {
        this.coverageType = coverageType;
    }

    public String getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public String getVisitId() {
        return visitNumber;
    }

    public String getClinicCode() {
        return clinicCode;
    }

    public void setClinicCode(String clinicCode) {
        this.clinicCode = clinicCode;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
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

    public String getPatientIdentity() {
        return patientIdentity;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Float getTotalPatientPaid() {
        return totalPatientPaid;
    }

    public void setTotalPatientPaid(Float totalPatientPaid) {
        this.totalPatientPaid = totalPatientPaid;
    }

    public Float getChargedAmount() {
        return chargedAmount;
    }

    public void setChargedAmount(Float chargedAmount) {
        this.chargedAmount = chargedAmount;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public void setPatientIdentity(String patientIdentity) {
        this.patientIdentity = patientIdentity;
    }

    public void setItemCategoryCode(String itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Float getPatientPayable() {
        return patientPayable;
    }

    public void setPatientPayable(Float patientPayable) {
        this.patientPayable = patientPayable;
    }

    public Float getCorporatePayable() {
        return corporatePayable;
    }

    public void setCorporatePayable(Float corporatePayable) {
        this.corporatePayable = corporatePayable;
    }

    public Float getInsurancePayable() {
        return insurancePayable;
    }

    public void setInsurancePayable(Float insurancePayable) {
        this.insurancePayable = insurancePayable;
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

    public String getPatientContactNumber() {
        return patientContactNumber;
    }

    public void setPatientContactNumber(String patientContactNumber) {
        this.patientContactNumber = patientContactNumber;
    }
}
