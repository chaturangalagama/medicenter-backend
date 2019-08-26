package com.ilt.cms.inventory.model.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.inventory.model.common.UOM;
import com.lippo.cms.util.CMSConstant;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public class Inventory {
    private String inventoryId;
    private String itemRefId;
    @Transient
    private String itemName;
    @Transient
    private String itemCode;
    private String batchNumber;
    private String baseUom;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate expiryDate;
    private boolean isLowStockLevel;
    /**
     * Values are multiples of 100
     */
    private int availableCount;


    public Inventory() {
        this.inventoryId = CommonUtils.idGenerator();
    }

    public Inventory(String itemRefId, String batchNumber, String baseUom, LocalDate expiryDate,
                     int availableCount, boolean isLowStockLevel) {
        this.inventoryId = CommonUtils.idGenerator();
        this.itemRefId = itemRefId;
        this.batchNumber = batchNumber;
        this.baseUom = baseUom;
        this.expiryDate = expiryDate;
        this.availableCount = availableCount;
        this.isLowStockLevel = isLowStockLevel;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getItemRefId() {
        return itemRefId;
    }

    public void setItemRefId(String itemRefId) {
        this.itemRefId = itemRefId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public boolean isLowStockLevel() {
        return isLowStockLevel;
    }

    public void setLowStockLevel(boolean lowStockLevel) {
        isLowStockLevel = lowStockLevel;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId='" + inventoryId + '\'' +
                ", itemRefId='" + itemRefId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", baseUom='" + baseUom + '\'' +
                ", expiryDate=" + expiryDate +
                ", isLowStockLevel=" + isLowStockLevel +
                ", availableCount=" + availableCount +
                '}';
    }
}
