package com.lippo.cms.inventory.model;

import java.sql.Date;

public class PatientRegisterDetail {
    private int patientRegisterDetailId;
    private int patientRegisterId;
    private int chargeId;
    private String batchNo;
    private Date expiryDate;
    private double quantity;
    private double paymentOriginal;
    private double paymentAmended;
    private double cost;
    private Date createdAt;
    private int createdUserId;
    private Date updatedAt;
    private int updatedUserId;

    public PatientRegisterDetail() {}

    public PatientRegisterDetail(
        int patientRegisterDetailId,
        int patientRegisterId,
        int chargeId,
        String batchNo,
        Date expiryDate,
        double quantity,
        double paymentOriginal,
        double paymentAmended,
        double cost,
        Date createdAt,
        int createdUserId,
        Date updatedAt,
        int updatedUserId
    ) {
        this.patientRegisterDetailId = patientRegisterDetailId;
        this.patientRegisterId = patientRegisterId;
        this.chargeId = chargeId;
        this.batchNo = batchNo;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.paymentOriginal = paymentOriginal;
        this.paymentAmended = paymentAmended;
        this.cost = cost;
        this.createdAt = createdAt;
        this.createdUserId  = createdUserId;
        this.updatedAt = updatedAt;
        this.updatedUserId = updatedUserId;
    }

    public int getPatientRegisterDetailId() {
        return patientRegisterDetailId;
    }
    public void setPatientRegisterDetailId(int patientRegisterDetailId) {
        this.patientRegisterDetailId = patientRegisterDetailId;
    }

    public int getPatientRegisterId() {
        return patientRegisterId;
    }
    public void setPatientRegisterId(int patientRegisterId) {
        this.patientRegisterId = patientRegisterId;
    }

    public int getChargeId() {
        return chargeId;
    }
    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPaymentOriginal() {
        return paymentOriginal;
    }
    public void setPaymentOriginal(double paymentOriginal) {
        this.paymentOriginal = paymentOriginal;
    }

    public double getPaymentAmended() {
        return paymentAmended;
    }
    public void setPaymentAmended(double paymentAmended) {
        this.paymentAmended = paymentAmended;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getCreatedUserId() {
        return createdUserId;
    }
    public void setCreatedUserId(int createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUpdatedUserId() {
        return updatedUserId;
    }
    public void setUpdatedUserId(int updatedUserId) {
        this.updatedUserId = updatedUserId;
    }
}
