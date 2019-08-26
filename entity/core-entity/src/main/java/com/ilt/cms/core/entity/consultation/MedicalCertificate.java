package com.ilt.cms.core.entity.consultation;

import java.time.LocalDate;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class MedicalCertificate {

    public enum Half {
        AM_LAST, PM_FIRST

    }

    private String purpose;
    private LocalDate startDate;
    private double numberOfDays;
    private String referenceNumber;
    private String remark;
    private Half halfDayOption;

    public MedicalCertificate() {
    }

    public MedicalCertificate(String purpose, LocalDate startDate, double numberOfDays) {
        this.purpose = purpose;
        this.startDate = startDate;
        this.numberOfDays = numberOfDays;
    }

    public MedicalCertificate(String purpose, LocalDate startDate, double numberOfDays, String referenceNumber, String remark, Half halfDayOption) {
        this.purpose = purpose;
        this.startDate = startDate;
        this.numberOfDays = numberOfDays;
        this.referenceNumber = referenceNumber;
        this.remark = remark;
        this.halfDayOption = halfDayOption;
    }

    public boolean areParametersValid() {
        return isStringValid(purpose) && startDate != null;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public double getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(double numberOfDays) {
        this.numberOfDays = numberOfDays;
    }


    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Half getHalfDayOption() {
        return halfDayOption;
    }

    public void setHalfDayOption(Half halfDayOption) {
        this.halfDayOption = halfDayOption;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "MedicalCertificate{" +
                "purpose='" + purpose + '\'' +
                ", startDate=" + startDate +
                ", numberOfDays=" + numberOfDays +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", remark='" + remark + '\'' +
                ", halfDayOption=" + halfDayOption +
                '}';
    }
}
