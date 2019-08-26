package com.ilt.cms.inventory.model.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.inventory.model.inventory.Inventory;
import com.ilt.cms.inventory.model.inventory.enums.StockTakeStatus;
import com.lippo.cms.util.CMSConstant;
import com.lippo.commons.util.CommonUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class StockCountItem {
    private String id;
    private String inventoryId;
    private String itemRefId;
    private String itemCode;
    private String itemName;
    private String curBatchNumber;
    private String curUom;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate curExpiryDate;
    private double availableCount;
    private String batchNumber;
    private String baseUom;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate expiryDate;

    private Double firstQuantity;

    private Double secondQuantity;

    private String purposeOfAdjustment;

    private String reason;

    public boolean checkValidate(StockTakeStatus stockTakeStatus){
        switch (stockTakeStatus){
            case IN_PROCESS_FIRST_STOCK_TAKE:
                return isStringValid(itemRefId, batchNumber, baseUom) && expiryDate != null;
            case IN_PROCESS_SECOND_STOCK_TAKE:
                return isStringValid(itemRefId, batchNumber, baseUom) && expiryDate != null && firstQuantity != null;
            case COMPLETED:
                return isStringValid(itemRefId, batchNumber, baseUom) && expiryDate != null && firstQuantity != null && secondQuantity != null;
                default:
                    return false;
        }
    }

    public StockCountItem() {
        this.id = CommonUtils.idGenerator();
    }

    public StockCountItem(String inventoryId, String itemCode, String itemName, String itemRefId,
                          String curBatchNumber, String curUom, LocalDate curExpiryDate, int availableCount) {
        this.id = CommonUtils.idGenerator();
        this.inventoryId = inventoryId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemRefId = itemRefId;
        this.curBatchNumber = curBatchNumber;
        this.curUom = curUom;
        this.curExpiryDate = curExpiryDate;
        this.batchNumber = curBatchNumber;
        this.baseUom = curUom;
        this.expiryDate = curExpiryDate;
        this.availableCount = availableCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
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

    public String getCurBatchNumber() {
        return curBatchNumber;
    }

    public void setCurBatchNumber(String curBatchNumber) {
        this.curBatchNumber = curBatchNumber;
    }

    public String getCurUom() {
        return curUom;
    }

    public void setCurUom(String curUom) {
        this.curUom = curUom;
    }

    public LocalDate getCurExpiryDate() {
        return curExpiryDate;
    }

    public void setCurExpiryDate(LocalDate curExpiryDate) {
        this.curExpiryDate = curExpiryDate;
    }

    public double getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(double availableCount) {
        this.availableCount = availableCount;
    }

    public Double getFirstQuantity() {
        return firstQuantity;
    }

    public void setFirstQuantity(Double firstQuantity) {
        this.firstQuantity = firstQuantity;
    }

    public Double getSecondQuantity() {
        return secondQuantity;
    }

    public void setSecondQuantity(Double secondQuantity) {
        this.secondQuantity = secondQuantity;
    }

    public String getPurposeOfAdjustment() {
        return purposeOfAdjustment;
    }

    public void setPurposeOfAdjustment(String purposeOfAdjustment) {
        this.purposeOfAdjustment = purposeOfAdjustment;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getItemRefId() {
        return itemRefId;
    }

    public void setItemRefId(String itemRefId) {
        this.itemRefId = itemRefId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getBaseUom() {
        return baseUom;
    }

    public void setBaseUom(String baseUom) {
        this.baseUom = baseUom;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "StockCountItem{" +
                "id='" + id + '\'' +
                "inventoryId='" + inventoryId + '\'' +
                ", itemRefId='" + itemRefId + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", curBatchNumber='" + curBatchNumber + '\'' +
                ", curUom='" + curUom + '\'' +
                ", curExpiryDate=" + curExpiryDate +
                ", availableCount=" + availableCount +
                ", batchNumber='" + batchNumber + '\'' +
                ", baseUom='" + baseUom + '\'' +
                ", expiryDate=" + expiryDate +
                ", firstQuantity=" + firstQuantity +
                ", secondQuantity=" + secondQuantity +
                ", purposeOfAdjustment='" + purposeOfAdjustment + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
