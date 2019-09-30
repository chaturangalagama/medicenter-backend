package com.ilt.cms.core.entity.medical;

import com.ilt.cms.core.entity.sales.ItemPriceAdjustment;
import com.lippo.commons.util.CommonUtils;

import java.time.LocalDate;

public class DispatchItem {

    private String purchasedId;
    private String itemId;
    private String dosageUom;
    private String instruct;
    private String dosageInstruction;
    private int duration;
    private int dosage;
    private int quantity;
    private int oriTotalPrice;
    private String batchNo;
    private LocalDate expiryDate;
    private ItemPriceAdjustment itemPriceAdjustment;
    private String remarks;

    public DispatchItem() {
    }

    public DispatchItem(String purchasedId, String itemId, String dosageUom, String instruct, int duration, int dosage,
                        int quantity, int oriTotalPrice, String batchNo, LocalDate expiryDate,
                        ItemPriceAdjustment itemPriceAdjustment, String remarks, String dosageInstruction) {
        this.purchasedId = purchasedId;
        this.itemId = itemId;
        this.dosageUom = dosageUom;
        this.instruct = instruct;
        this.duration = duration;
        this.dosage = dosage;
        this.quantity = quantity;
        this.oriTotalPrice = oriTotalPrice;
        this.batchNo = batchNo;
        this.expiryDate = expiryDate;
        this.itemPriceAdjustment = itemPriceAdjustment;
        this.remarks = remarks;
        this.dosageInstruction = dosageInstruction;
    }

    public boolean areParameterValid() {
        return CommonUtils.isStringValid(itemId);
    }

    public DispatchItem copy(DispatchItem dispatchItem) {
        purchasedId = dispatchItem.purchasedId;
        itemId = dispatchItem.itemId;
        dosageUom = dispatchItem.dosageUom;
        instruct = dispatchItem.instruct;
        duration = dispatchItem.duration;
        dosage = dispatchItem.dosage;
        quantity = dispatchItem.quantity;
        oriTotalPrice = dispatchItem.oriTotalPrice;
        batchNo = dispatchItem.batchNo;
        expiryDate = dispatchItem.expiryDate;
        itemPriceAdjustment = dispatchItem.itemPriceAdjustment;
        remarks = dispatchItem.remarks;
        this.dosageInstruction = dispatchItem.dosageInstruction;
        return this;
    }

    public String getPurchasedId() {
        return purchasedId;
    }

    public void setPurchasedId(String purchasedId) {
        this.purchasedId = purchasedId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDosageUom() {
        return dosageUom;
    }

    public void setDosageUom(String dosageUom) {
        this.dosageUom = dosageUom;
    }

    public String getInstruct() {
        return instruct;
    }

    public void setInstruct(String instruct) {
        this.instruct = instruct;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOriTotalPrice() {
        return oriTotalPrice;
    }

    public void setOriTotalPrice(int oriTotalPrice) {
        this.oriTotalPrice = oriTotalPrice;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ItemPriceAdjustment getItemPriceAdjustment() {
        return itemPriceAdjustment;
    }

    public void setItemPriceAdjustment(ItemPriceAdjustment itemPriceAdjustment) {
        this.itemPriceAdjustment = itemPriceAdjustment;
    }

    public String getDosageInstruction() {
        return dosageInstruction;
    }

    public void setDosageInstruction(String dosageInstruction) {
        this.dosageInstruction = dosageInstruction;
    }

    @Override
    public String toString() {
        return "DispatchItem{" +
                "purchasedId='" + purchasedId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", dosageUom='" + dosageUom + '\'' +
                ", instruct='" + instruct + '\'' +
                ", dosageInstruction='" + dosageInstruction + '\'' +
                ", duration=" + duration +
                ", dosage=" + dosage +
                ", quantity=" + quantity +
                ", oriTotalPrice=" + oriTotalPrice +
                ", batchNo='" + batchNo + '\'' +
                ", expiryDate=" + expiryDate +
                ", itemPriceAdjustment=" + itemPriceAdjustment +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
