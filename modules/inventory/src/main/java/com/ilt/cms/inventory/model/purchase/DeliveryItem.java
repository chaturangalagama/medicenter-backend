package com.ilt.cms.inventory.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public abstract class DeliveryItem {
    protected String id;
    protected String itemRefId;
    protected String uom;
    protected String batchNumber;
    protected int quantity;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    protected LocalDate expiryDate;
    protected Boolean isCountInStock;

    public boolean isSameItem (DeliveryItem deliveryItem){
        return id == null ? false
                : this.id.equals(deliveryItem.getId());
    }

    public boolean equal(DeliveryItem deliveryItem){
        if(this.id != null && deliveryItem.getId() != null){
            return this.id.equals(deliveryItem.getId())
                    && this.itemRefId.equals(deliveryItem.getItemRefId())
                            && this.uom == null ? deliveryItem.getUom() == null ? true : false
                            : this.uom.equals(deliveryItem.uom)
                            && quantity == deliveryItem.getQuantity()
                            && this.expiryDate == null ? deliveryItem.getExpiryDate() == null ? true : false
                            :expiryDate.equals(deliveryItem.getExpiryDate())
                            && batchNumber == null ? deliveryItem.getBatchNumber() == null ? true : false
                            : this.batchNumber.equals(deliveryItem.getBatchNumber());
        }
        if(this.id == null && deliveryItem.getId() == null){
            return this.itemRefId.equals(deliveryItem.getItemRefId())
                    && this.uom == null ? deliveryItem.getUom() == null ? true : false
                    : this.uom.equals(deliveryItem.uom)
                    && quantity == deliveryItem.getQuantity()
                    && this.expiryDate == null ? deliveryItem.getExpiryDate() == null ? true : false
                    :expiryDate.equals(deliveryItem.getExpiryDate())
                    && batchNumber == null ? deliveryItem.getBatchNumber() == null ? true : false
                    : this.batchNumber.equals(deliveryItem.getBatchNumber());
        }
        return false;
    }

    public abstract boolean checkValidate();

    public boolean notCountInStock(){
        return isCountInStock == null ? true : !isCountInStock;
    }

    public DeliveryItem() {
        this.isCountInStock = false;
    }

    public DeliveryItem(String id, String itemRefId, String uom, String batchNumber, int quantity, LocalDate expiryDate) {
        this.id = id;
        this.itemRefId = itemRefId;
        this.uom = uom;
        this.batchNumber = batchNumber;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.isCountInStock = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemRefId() {
        return itemRefId;
    }

    public void setItemRefId(String itemRefId) {
        this.itemRefId = itemRefId;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getCountInStock() {
        return isCountInStock;
    }

    public void setCountInStock(Boolean countInStock) {
        isCountInStock = countInStock;
    }
}
