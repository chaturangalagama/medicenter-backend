package com.ilt.cms.inventory.model.purchase;

import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.common.UnitPrice;

import java.util.Objects;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class RequestItem {

    private String itemRefId;
    private String supplierId;
    private String uom;
    private int quantity;
    private UnitPrice unitPrice;

    public boolean checkValidate(){
        return isStringValid(itemRefId, uom) && quantity > 0 && unitPrice != null;
    }

    public RequestItem() {
    }

    public RequestItem(String itemRefId, String uom, int quantity) {
        this.itemRefId = itemRefId;
        this.uom = uom;
        this.quantity = quantity;
    }

    public RequestItem(String itemRefId, String supplierId, String uom, int quantity, UnitPrice unitPrice) {
        this.itemRefId = itemRefId;
        this.supplierId = supplierId;
        this.uom = uom;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getItemRefId() {
        return itemRefId;
    }

    public void setItemRefId(String itemRefId) {
        this.itemRefId = itemRefId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
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

    public UnitPrice getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(UnitPrice unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestItem that = (RequestItem) o;
        return Objects.equals(itemRefId, that.itemRefId) &&
                Objects.equals(uom, that.uom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemRefId, uom);
    }

    @Override
    public String toString() {
        return "RequestItem{" +
                "itemRefId='" + itemRefId + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", uom='" + uom + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
