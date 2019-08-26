package com.lippo.cms.inventory.model;

import java.sql.Date;

public class DrugDispensing {
    private int drugDispensingId;
    private int patientRegisterDetailId;
    private int chargeId;
    private int clinicId;
    private int inventoryDetailId;
    private double quantity;
    private double stockAmount;
    private Date createdAt;
    private int createdUserId;
    private Date updatedAt;
    private int updatedUserId;

    public DrugDispensing() {}

    public DrugDispensing(
        int drugDispensingId,
        int patientRegisterDetailId,
        int chargeId,
        int clinicId,
        int inventoryDetailId,
        double quantity,
        double stockAmount,
        Date createdAt,
        int createdUserId,
        Date updatedAt,
        int updatedUserId
    ) {
        this.drugDispensingId = drugDispensingId;
        this.patientRegisterDetailId = patientRegisterDetailId;
        this.chargeId = chargeId;
        this.clinicId = clinicId;
        this.inventoryDetailId = inventoryDetailId;
        this.quantity = quantity;
        this.stockAmount = stockAmount;
        this.createdAt = createdAt;
        this.createdUserId  = createdUserId;
        this.updatedAt = updatedAt;
        this.updatedUserId = updatedUserId;
    }

    public int getDrugDispensingId() {
        return drugDispensingId;
    }
    public void setDrugDispensingId(int drugDispensingId) {
        this.drugDispensingId = drugDispensingId;
    }

    public int getPatientRegisterDetailId() {
        return patientRegisterDetailId;
    }
    public void setPatientRegisterDetailId(int patientRegisterDetailId) {
        this.patientRegisterDetailId = patientRegisterDetailId;
    }

    public int getChargeId() {
        return chargeId;
    }
    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public int getClinicId() {
        return clinicId;
    }
    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public int getInventoryDetailId() {
        return inventoryDetailId;
    }
    public void setInventoryDetailId(int inventoryDetailId) {
        this.inventoryDetailId = inventoryDetailId;
    }

    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getStockAmount() {
        return stockAmount;
    }
    public void setStockAmount(double stockAmount) {
        this.stockAmount = stockAmount;
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
