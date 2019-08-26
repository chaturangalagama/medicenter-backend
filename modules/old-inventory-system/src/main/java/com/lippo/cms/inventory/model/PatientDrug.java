package com.lippo.cms.inventory.model;

import java.sql.Date;

public class PatientDrug {
    private int id;
    private int patientRegisterDetailId;
    private int chargeId;
    private double quantity;
    private double cost;
    private Date createdAt;
    private int createdUserId;
    private Date updatedAt;
    private int updatedUserId;

    public PatientDrug() {}

    public PatientDrug(
        int id,
        int patientRegisterDetailId,
        int chargeId,
        double quantity,
        double cost,
        Date createdAt,
        int createdUserId,
        Date updatedAt,
        int updatedUserId
    ) {
        this.id = id;
        this.patientRegisterDetailId = patientRegisterDetailId;
        this.chargeId = chargeId;
        this.quantity = quantity;
        this.cost = cost;
        this.createdAt = createdAt;
        this.createdUserId  = createdUserId;
        this.updatedAt = updatedAt;
        this.updatedUserId = updatedUserId;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
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
