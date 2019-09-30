package com.ilt.cms.core.entity.sales;

import java.time.LocalDateTime;

/**
 * The consuming tracking records of purchased package items of a particular case, are recording in this model.
 * Once the each item consumed, then the parameter "utilize" should be true.
 */
public class Dispatch {

    private String itemRefId;
    private String purchasedId;
    private String itemCode;
    private String itemName;
    private boolean utilize;
    private boolean payable;
    private LocalDateTime utilizedDate;

    public Dispatch() {
    }

    public Dispatch(String id, String code, String name, String purchasedId) {
        this.itemRefId = id;
        this.itemCode = code;
        this.itemName = name;
        this.purchasedId = purchasedId;
    }

    public String getPurchasedId() {
        return purchasedId;
    }

    public void setPurchasedId(String purchasedId) {
        this.purchasedId = purchasedId;
    }

    public LocalDateTime getUtilizedDate() {
        return utilizedDate;
    }

    public void setUtilizedDate(LocalDateTime utilizedDate) {
        this.utilizedDate = utilizedDate;
    }

    public String getItemRefId() {
        return itemRefId;
    }

    public void setItemRefId(String itemRefId) {
        this.itemRefId = itemRefId;
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

    public boolean isUtilize() {
        return utilize;
    }

    public void setUtilize(boolean utilize) {
        this.utilize = utilize;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    @Override
    public String toString() {
        return "Dispatch{" +
                "itemRefId='" + itemRefId + '\'' +
                ", purchasedId='" + purchasedId + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", utilize=" + utilize +
                ", payable=" + payable +
                ", utilizedDate=" + utilizedDate +
                '}';
    }
}
