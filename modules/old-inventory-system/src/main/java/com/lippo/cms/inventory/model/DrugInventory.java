package com.lippo.cms.inventory.model;

import java.sql.Date;

public class DrugInventory {
    private int id;
    private int chargeId;
    private int clinicId;
    private double quantity;
    private double amount;
    private double totalQuantity;
    private double totalAmount;
    private Date createdAt;
    private int createdUserId;
    private Date updatedAt;
    private int updatedUserId;

    public DrugInventory() {}

    public DrugInventory(
        int id,
        int chargeId,
        int clinicId,
        double quantity,
        double amount,
        double totalQuantity,
        double totalAmount,
        Date createdAt,
        int createdUserId,
        Date updatedAt,
        int updatedUserId
    ) {
        this.id = id;
        this.chargeId = chargeId;
        this.clinicId = clinicId;
        this.quantity = quantity;
        this.amount = amount;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
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

    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }
    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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
