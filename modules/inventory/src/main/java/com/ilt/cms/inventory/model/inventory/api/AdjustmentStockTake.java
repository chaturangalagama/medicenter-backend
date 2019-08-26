package com.ilt.cms.inventory.model.inventory.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class AdjustmentStockTake {

    private String itemCode;

    private String batchNo;

    private String uom;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate expiryDate;

    private int newStockLevel;

    private String purposeOfAdjustment;

    private String remark;

    public AdjustmentStockTake() {
    }

    public AdjustmentStockTake(String itemCode, String batchNo, String uom, LocalDate expiryDate, int newStockLevel,
                               String purposeOfAdjustment, String remark) {
        this.itemCode = itemCode;
        this.batchNo = batchNo;
        this.uom = uom;
        this.expiryDate = expiryDate;
        this.newStockLevel = newStockLevel;
        this.purposeOfAdjustment = purposeOfAdjustment;
        this.remark = remark;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getNewStockLevel() {
        return newStockLevel;
    }

    public void setNewStockLevel(int newStockLevel) {
        this.newStockLevel = newStockLevel;
    }

    public String getPurposeOfAdjustment() {
        return purposeOfAdjustment;
    }

    public void setPurposeOfAdjustment(String purposeOfAdjustment) {
        this.purposeOfAdjustment = purposeOfAdjustment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
