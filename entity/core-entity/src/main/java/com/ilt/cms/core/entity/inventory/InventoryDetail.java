package com.ilt.cms.core.entity.inventory;

import java.util.Date;

public class InventoryDetail {
    private int inventoryDetailId;
    private int inventoryId;
    private int clinicId;
    private int chargeId;
    private Date expiryDate;
    private String batchNo = "";
    private double quantity = 0;
    private double quantityTotal = 0;
    private double amount;
    private double itemAmount;
    private double itemCost;
    private double remainingQuantity = 0;
    private double remainingAmount = 0;
    private Date createdAt;
    private int createdUserId;
    private Date updatedAt;
    private int updatedUserId;

    private String drugCode;
    private double totalQuantity = 0;
    private double totalAmount = 0;
    private double cost;
    private boolean mixingFlag;

    private String itemId;
    private int inventoryUsageIndex;
    private int patientRegisterDetailId;

    public InventoryDetail() {}

    public InventoryDetail(
            int inventoryDetailId,
            int inventoryId,
            int clinicId,
            int chargeId,
            Date expiryDate,
            String batchNo,
            double quantity,
            double quantityTotal,
            double amount,
            double itemAmount,
            double itemCost,
            double remainingQuantity,
            double remainingAmount,
            Date createdAt,
            int createdUserId,
            Date updatedAt,
            int updatedUserId,

            String drugCode,
            double totalQuantity,
            double totalAmount,
            double cost,
            boolean mixingFlag
    ) {
        this.inventoryDetailId = inventoryDetailId;
        this.inventoryId = inventoryId;
        this.clinicId = clinicId;
        this.chargeId = chargeId;
        this.expiryDate = expiryDate;
        this.batchNo = batchNo;
        this.quantity = quantity;
        this.quantityTotal = quantityTotal;
        this.amount = amount;
        this.itemAmount = itemAmount;
        this.itemCost = itemCost;
        this.remainingQuantity = remainingQuantity;
        this.remainingAmount = remainingAmount;
        this.createdAt = createdAt;
        this.createdUserId  = createdUserId;
        this.updatedAt = updatedAt;
        this.updatedUserId = updatedUserId;

        this.drugCode = drugCode;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.cost = cost;
        this.mixingFlag = mixingFlag;

        this.itemId = "";
    }

    public int getInventoryDetailId() {
        return inventoryDetailId;
    }
    public void setInventoryDetailId(int inventoryDetailId) {
        this.inventoryDetailId = inventoryDetailId;
    }

    public int getInventoryId() {
        return inventoryId;
    }
    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getClinicId() {
        return clinicId;
    }
    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public int getChargeId() {
        return chargeId;
    }
    public void setChargeId(int chargeId) {
        this.chargeId = chargeId;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantityTotal() {
        return quantityTotal;
    }
    public void setQuantityTotal(double quantityTotal) {
        this.quantityTotal = quantityTotal;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getItemAmount() {
        return itemAmount;
    }
    public void setItemAmount(double itemAmount) {
        this.itemAmount = itemAmount;
    }

    public double getItemCost() {
        return itemCost;
    }
    public void setItemCost(double itemCost) {
        this.itemCost = itemCost;
    }

    public double getRemainingQuantity() {
        return remainingQuantity;
    }
    public void setRemainingQuantity(double remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }
    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
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

    public String getDrugCode() {
        return drugCode;
    }
    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getInventoryUsageIndex() {
        return inventoryUsageIndex;
    }
    public void setInventoryUsageIndex(int inventoryUsageIndex) {
        this.inventoryUsageIndex = inventoryUsageIndex;
    }

    public int getPatientRegisterDetailId() {
        return patientRegisterDetailId;
    }
    public void setPatientRegisterDetailId(int patientRegisterDetailId) {
        this.patientRegisterDetailId = patientRegisterDetailId;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isMixingFlag() {
        return mixingFlag;
    }
    public void setMixingFlag(boolean mixingFlag) {
        this.mixingFlag = mixingFlag;
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
}
